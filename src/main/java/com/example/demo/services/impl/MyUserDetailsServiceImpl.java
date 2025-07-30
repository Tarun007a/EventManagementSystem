package com.example.demo.services.impl;

import com.example.demo.entities.User;
import com.example.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        System.out.println("username = " + username);
        Optional<User> user = userRepo.findByEmail(username);
        if(user.isEmpty())throw new UsernameNotFoundException("user not found with username from MyUserDetailsService loadUserByUsername");
        return user.get();
    }
}
