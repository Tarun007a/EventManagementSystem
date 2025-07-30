package com.example.demo.controllers;

import com.example.demo.dtos.EventDto;
import com.example.demo.dtos.UserDto;
import com.example.demo.services.EventService;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private UserService userService;
    public AuthController(UserService userService){
        this.userService = userService;
    }
    @PostMapping("/user/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto){

        UserDto savedUserDto = userService.saveUserDto(userDto);
        return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
    }

    @PostMapping("/event-admin/register")
    public ResponseEntity<UserDto> responseEventManager(@Valid @RequestBody UserDto userDto){
        UserDto savedUserDto = userService.saveEventAdmin(userDto);
        return new ResponseEntity<>(savedUserDto, HttpStatus.CREATED);
    }
}
