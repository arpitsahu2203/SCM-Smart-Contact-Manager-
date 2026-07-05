package org.arpitsahu.smc.Config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//THIS CLASS HOLD THE CONFIGURATION OF Cloudinary THAT WILL HELP US USE Cloudinary ON OUR APPLICATION
@Configuration
public class AppConfig {

    //@Value:This allows us to call values stored in Application.properties
    @Value("${cloudinary.cloud.name}")
    public String cloud_name;

    @Value("${cloudinary.cloud.api.key}")
    public String cloud_api;

    @Value("${cloudinary.cloud.api.secret}")
    public String cloud_secret;

    //all these values are passed as an object to that Cloudinary class which will help us use Cloudinary in our application
    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(
                ObjectUtils.asMap(
                        "cloud_name", cloud_name,
                        "api_key", cloud_api,
                        "api_secret", cloud_secret
                )
        );
    }
}
