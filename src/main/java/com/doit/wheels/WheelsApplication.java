package com.doit.wheels;

import com.doit.wheels.auth.SecurityConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Import({SecurityConfiguration.class})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class WheelsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WheelsApplication.class, args);
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:locale/messages");
        messageSource.setCacheSeconds(3600); //refresh cache once per hour
        return messageSource;
    }

}
