package smp.group.task.tracking.Config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class Webconfig implements  WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Allow CORS for all paths
                .allowedOrigins("http://localhost:5173") // Allow only this origin
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specify allowed methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true); // Allow cookies and credentials
    }

    @Bean
    public Cloudinary  getCloudinary(){
        Map map = new HashMap<>();
        map.put("cloud_name" ,"dzu0bryd5");
        map.put("api_key" ,"771298759177374");
        map.put("api_secret" ,"sjMb-6KZRPGB9jf8pvGCT0yOX0Q");
        map.put("secure" , true);

        return new Cloudinary(map);

    }

}
