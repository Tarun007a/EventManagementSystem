package com.example.demo.controllers;

import com.example.demo.dtos.EventDto;
import com.example.demo.dtos.UserDto;
import com.example.demo.entities.User;
import com.example.demo.helper.AppConstants;
import com.example.demo.services.EventService;
import com.example.demo.services.UserService;
import com.example.demo.services.impl.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/")
@PreAuthorize("hasRole('USER')")
public class UserController {
    private UserService userService;

    private EventService eventService;

    private AuthUtil authUtil;

    public UserController(UserService userService, AuthUtil authUtil, EventService eventService){
        this.userService = userService;
        this.authUtil = authUtil;
        this.eventService = eventService;
    }
    @GetMapping("/home")
    public String homePage(){
        return "User home page view";
    }

    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto){
        String email = authUtil.getEmailOfLoggedUser();
        if(!userDto.getEmail().equals(email))return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
        UserDto updatedUser = userService.updateUserDto(userDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(){
        String email = authUtil.getEmailOfLoggedUser();
        userService.deleteUserByEmail(email);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.NO_CONTENT);
    }

    //remove when deploying or create a admin role who can do this
//    @GetMapping("/getAll")
//    public ResponseEntity<List<User>> getAllUser(){
//        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
//    }

    @GetMapping("/get")
    public ResponseEntity<List<EventDto>> getEvents(
            @RequestParam(value = "page", defaultValue = AppConstants.page) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.size) int size
    ){
        List<EventDto> list = eventService.getAllEvents(page, size);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<EventDto>> getByKeyword(
            @RequestParam(value = "keyword", defaultValue = AppConstants.keyword) String keyword,
            @RequestParam(value = "page", defaultValue = AppConstants.page) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.size) int size
    ){
        List<EventDto> list = eventService.getByKeyword(keyword, page, size);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PutMapping("/register-event/{id}")
    public ResponseEntity<EventDto> registerEvent(@PathVariable Long id){
        return new ResponseEntity<>(eventService.registerInEvent(id), HttpStatus.ACCEPTED);
    }

    @GetMapping("/get-registered-events")
    public ResponseEntity<List<EventDto>> getRegisteredEvents(
            @RequestParam(value = "page", defaultValue = AppConstants.page) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.size) int size
    ){
        return new ResponseEntity<>(eventService.getRegisteredEvents(page, size), HttpStatus.OK);
    }
}
