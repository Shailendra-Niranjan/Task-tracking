package smp.group.task.tracking.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {

    private final SecretKey secretKey ;

    // Constructor: Generate the key once and store it
    public JWTService() {
        //    @Value("${jwt.secret}")
        String secretKeyBase64 = "U2FsdGVkX19gZWF1Z9cA4O6qR9cB2b8sbT0t3U8IwOQ";
        this.secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKeyBase64));
    }

    // Getter to retrieve the key for signing or verification
    public SecretKey getKey() {
        return this.secretKey;
    }

    public String  generateToken(String  userName  , String role){
        Map<String  ,Object> claims = new HashMap<>();
        claims.put("role" , role);
       return Jwts.builder()
                .claims()
                .add(claims)
                .subject(userName)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ( 15 * 60 * 1000)))
                .and()
                .signWith(getKey())
                .compact();
    }


// public Key keyGenerator(){
//      try {
//          KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");
//          return Keys.hmacShaKeyFor(generator.generateKey().getEncoded());
//      } catch (NoSuchAlgorithmException e) {
//          throw new RuntimeException(e);
//      }
//
//  }

    public String extractUserName(String token) {

        return extractClaim(token , Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims , T> claimsTFunction) {
        final Claims claims = extractAllClaim(token);
        return  claimsTFunction.apply(claims);
    }

    private Claims extractAllClaim(String token)  throws SignatureException{

            return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token).getPayload();

    }
    public String getPermissionsFromToken(String token) throws SignatureException{
//        Claims claims = Jwts.parser()
//                .setSigningKey(getKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//        return (String) claims.get("permission");

            Claims claims = Jwts.parser()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String premission = (String) claims.get("role");
            if (premission ==null){
                throw new SignatureException("sign");
            }
            return  premission;



    }



//    public SecretKey getKey()  {
//        try {
//            KeyGenerator generator = KeyGenerator.getInstance("HmacSHA256");
//            return generator.generateKey();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public boolean validateToken(String token ) {
        final  String userName = extractUserName(token);

        return  !isTokenExpired(token);

    }

    private boolean isTokenExpired(String token) {

        return extractExpiraton(token).before(new Date());
    }

    private Date extractExpiraton(String token) {
        return extractClaim(token , Claims::getExpiration);
    }
}
