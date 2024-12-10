package smp.group.task.tracking.Execptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class CustomExceptionHandler {

    // Handle BadCredentialsException with ProblemDetail
    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentialsException(BadCredentialsException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        problemDetail.setProperty("access denied", "Authentication Failure");
        return problemDetail;
    }

    // Handle AccessDeniedException with ProblemDetail
    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(AccessDeniedException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
        problemDetail.setProperty("access denied", "Not authorized");
        return problemDetail;
    }

    // Handle SignatureException with ProblemDetail
    @ExceptionHandler(SignatureException.class)
    public ProblemDetail handleSignatureException(SignatureException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
        problemDetail.setProperty("access denied", "JWT Signature not valid!");
        return problemDetail;
    }

    // Handle ExpiredJwtException with ProblemDetail
    @ExceptionHandler(ExpiredJwtException.class)
    public ProblemDetail handleExpiredJwtException(ExpiredJwtException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, e.getMessage());
        problemDetail.setProperty("access denied", "JWT token already expired!");
        return problemDetail;
    }
    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleBadRequest(ResponseStatusException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), ex.getReason());
        return problemDetail;
    }


//    // Handle BadRequestException with ProblemDetail
//    @ExceptionHandler(BadRequestException.class)
//    public ProblemDetail handleBadRequestex(BadRequestException e) {
//        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
//        problemDetail.setProperty("access denied", "Bad Request");
//        return problemDetail;
//    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception e) {
        System.out.println(e.getClass());
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setProperty("error", "The requested endpoint could not be found: ");
        return problemDetail;
    }
}
