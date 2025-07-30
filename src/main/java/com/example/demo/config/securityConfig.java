package com.example.demo.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class securityConfig {
    OAuthSuccessHandler oAuthSuccessHandler;
    UserDetailsService userDetailsService;

    public securityConfig(OAuthSuccessHandler oAuthSuccessHandler, UserDetailsService userDetailsService){
        this.oAuthSuccessHandler = oAuthSuccessHandler;
        this.userDetailsService = userDetailsService;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(customizer -> {
            customizer.disable();
        });
        httpSecurity.authenticationProvider(authenticationProvider());
        httpSecurity.authorizeHttpRequests(request ->{
            request.requestMatchers("/auth/**").permitAll();
           request.requestMatchers("/user/**", "/event-admin/**")
                   .authenticated();
           request.anyRequest().permitAll();
        });
        httpSecurity.httpBasic(withDefaults())
                    .formLogin(withDefaults());

        httpSecurity.oauth2Login(oauth2 -> {
            oauth2.loginPage("/login");
            oauth2.successHandler(oAuthSuccessHandler);
        });

        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
