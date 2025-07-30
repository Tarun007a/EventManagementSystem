package com.example.demo.mapper.Impl;

import com.example.demo.dtos.UserDto;
import com.example.demo.entities.User;
import com.example.demo.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class UserMapper implements Mapper<User, UserDto> {

    private ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto mapTo(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}
