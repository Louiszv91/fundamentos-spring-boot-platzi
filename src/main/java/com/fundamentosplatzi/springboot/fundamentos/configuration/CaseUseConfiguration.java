package com.fundamentosplatzi.springboot.fundamentos.configuration;

import com.fundamentosplatzi.springboot.fundamentos.caseUse.GetUser;
import com.fundamentosplatzi.springboot.fundamentos.caseUse.GetUserImplement;
import com.fundamentosplatzi.springboot.fundamentos.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaseUseConfiguration {
    @Bean
    GetUser getUser(UserService userService){
        return new GetUserImplement(userService);
    }
}
