package com.example.demo.services;

import com.example.demo.dtos.EventDto;
import com.example.demo.dtos.UserDto;
import com.example.demo.entities.User;
import org.springframework.http.HttpStatusCode;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User saveUser(User user);

    UserDto saveUserDto(UserDto user);

    UserDto updateUserDto(UserDto userDto);

    User updateUser(User user);

    Optional<User> getUserById(String id);

    List<User> getAllUsers();

    void deleteUser(String id);

    boolean isUserExist(String id);

    boolean isUserExistByEmail(String email);

    Optional<User> getUserByEmail(String email);

    void deleteUserByEmail(String email);

    UserDto saveEventAdmin(UserDto userDto);
}
