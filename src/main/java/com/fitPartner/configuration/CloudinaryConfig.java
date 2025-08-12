package com.fitPartner.configuration;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dmqa8d6yq",
                "api_key", "859326834471994",
                "api_secret", "z40TdCn7fvSlZeCZ26kxoLNWVcQ"
        ));
    }
}

