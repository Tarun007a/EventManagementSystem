package com.example.demo.mapper.Impl;

import com.example.demo.dtos.EventDto;
import com.example.demo.entities.Event;
import com.example.demo.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EventMapper implements Mapper<Event, EventDto> {
    private ModelMapper modelMapper;
    public EventMapper(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }
    @Override
    public EventDto mapTo(Event event) {
        return modelMapper.map(event, EventDto.class);
    }

    @Override
    public Event mapFrom(EventDto eventDto) {
        return modelMapper.map(eventDto, Event.class);
    }
}
