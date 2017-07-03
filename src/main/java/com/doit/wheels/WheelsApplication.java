package com.doit.wheels;

import com.doit.wheels.auth.SecurityConfiguration;
import com.doit.wheels.dao.entities.User;
import com.doit.wheels.dao.repositories.UserRepository;
import com.doit.wheels.utils.UserRoleEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Import({SecurityConfiguration.class})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class WheelsApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WheelsApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(WheelsApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(UserRepository repository) {
        return (args) ->{
            if (repository.findUserByUsername("admin") == null){
                User user = new User("admin", "$2a$06$IiW.kr4tFQvx0U1NQ40twu/G.coGTbUPT9kVGWRcvzQdQXdu8qqAC");
                user.setRole(UserRoleEnum.ADMIN);
                repository.save(user);
            }
        };
    }

    @Bean
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:locale/messages");
        messageSource.setCacheSeconds(3600); //refresh cache once per hour
        return messageSource;
    }
}
