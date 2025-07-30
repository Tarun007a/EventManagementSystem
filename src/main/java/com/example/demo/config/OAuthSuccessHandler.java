package com.example.demo.config;

import com.example.demo.entities.User;
import com.example.demo.repository.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {
    UserRepo userRepo;
    public OAuthSuccessHandler(UserRepo userRepo){
        this.userRepo = userRepo;
    }
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oAuth2AuthenticationToken.getPrincipal();

        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
        if (name == null) name = oAuth2User.getName();

        if(userRepo.findByEmail(email).isEmpty()){
            String id = UUID.randomUUID().toString();
            String password = UUID.randomUUID().toString();
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setPhoneNumber("no phone number update it");
            user.getRoleList().add("ROLE_USER");
            user.setUserId(id);
            userRepo.save(user);
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, "/home");
    }
}
