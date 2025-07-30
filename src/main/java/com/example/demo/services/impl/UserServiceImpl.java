package com.example.demo.services.impl;

import com.example.demo.dtos.UserDto;
import com.example.demo.entities.User;
import com.example.demo.mapper.Mapper;
import com.example.demo.repository.UserRepo;
import com.example.demo.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private UserRepo userRepo;
    private Mapper<User, UserDto> mapper;
    private PasswordEncoder passwordEncoder;

    private AuthUtil authUtil;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, Mapper<User, UserDto> mapper, AuthUtil authUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.authUtil = authUtil;
    }

    @Override
    public UserDto saveUserDto(UserDto userDto) {
        User newUser = mapper.mapFrom(userDto);
        String id = UUID.randomUUID().toString();
        newUser.getRoleList().add("ROLE_USER");
        newUser.setUserId(id);
        newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepo.save(newUser);
        UserDto savedUserDto = mapper.mapTo(newUser);
        savedUserDto.setPassword(null);
        return savedUserDto;
    }

    @Override
    public User saveUser(User user) {
        return userRepo.save(user);
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public Optional<User> getUserById(String id) {
        return userRepo.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return (List<User>) userRepo.findAll();
    }

    @Override
    public void deleteUser(String id) {
        userRepo.deleteById(id);
    }

    @Override
    public boolean isUserExist(String id) {
        return userRepo.existsById(id);
    }

    @Override
    public UserDto updateUserDto(UserDto userDto) {
        Optional<User> userOptional = userRepo.findByEmail(userDto.getEmail());
        if(userOptional.isEmpty()){
            return null;
        }
        User user = userOptional.get();
        user.setName(userDto.getName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        User savedUser = userRepo.save(user);
        return userDto;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    @Override
    public UserDto saveEventAdmin(UserDto userDto) {
        UserDto savedUserDto = this.saveUserDto(userDto);
        Optional<User> userOptional = this.getUserByEmail(savedUserDto.getEmail());
        if(userOptional.isEmpty())return savedUserDto;
        User user = userOptional.get();
        user.getRoleList().add("ROLE_EVENT_ADMIN");
        userRepo.save(user);
        return  savedUserDto;
    }

    @Override
    @Transactional
    public void deleteUserByEmail(String email) {
        userRepo.deleteByEmail(email);
        return;
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }


//
//
//    public UserRepo getUserRepo() {
//        return this.userRepo;
//    }
//
//    public BCryptPasswordEncoder getPasswordEncoder() {
//        return this.passwordEncoder;
//    }
//
//    public void setUserRepo(UserRepo userRepo) {
//        this.userRepo = userRepo;
//    }
//
//    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }


}
