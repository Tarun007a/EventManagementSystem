package com.example.demo.services.impl;

import com.example.demo.dtos.EventDto;
import com.example.demo.entities.Event;
import com.example.demo.entities.User;
import com.example.demo.mapper.Mapper;
import com.example.demo.repository.EventRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.services.EventService;
import com.example.demo.services.UserService;
import lombok.Data;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Data
@Service
public class EventServiceImpl implements EventService {
    private AuthUtil authUtil;
    private EventRepo eventRepo;
    private UserService userService;
    private Mapper<Event, EventDto> mapper;

    public EventServiceImpl(EventRepo eventRepo, Mapper<Event, EventDto> mapper, AuthUtil authUtil, UserService userService) {
        this.eventRepo = eventRepo;
        this.mapper = mapper;
        this.authUtil = authUtil;
        this.userService = userService;
    }

    @Override
    public EventDto createEvent(EventDto eventDto) {
        Event event = mapper.mapFrom(eventDto);
        event.setUserRegistered(0L);
        String email = authUtil.getEmailOfLoggedUser();
        User user = userService.getUserByEmail(email).get();
        user.getCreatedEvent().add(event);
        event.setCreatedBy(user);

        eventRepo.save(event);

        return mapper.mapTo(event);
    }

    @Override
    public void deleteEvent(Long id) {
        Optional<Event> eventOptional = eventRepo.findById(id);
        if(eventOptional.isEmpty())return;

        Event event = eventOptional.get();

        String email = authUtil.getEmailOfLoggedUser();
        User currLoggedUser = userService.getUserByEmail(email).get();

        if(!event.getCreatedBy().equals(currLoggedUser)){
            throw new AccessDeniedException("you are not allowed to delete this event");
        }

        User user = event.getCreatedBy();
        user.getCreatedEvent().remove(event);
        eventRepo.delete(event);
    }

    @Override
    public EventDto updateEvent(Long id, EventDto eventDto) {
        Optional<Event> eventOptional = eventRepo.findById(id);
        if (eventOptional.isEmpty()) return null;
        Event event = eventOptional.get();

        String email = authUtil.getEmailOfLoggedUser();
        User currLoggedUser = userService.getUserByEmail(email).get();

        if(!event.getCreatedBy().equals(currLoggedUser)){
            throw new AccessDeniedException("You are not allowed to modify this event");
        }

        event.setName(eventDto.getName());
        event.setLocation(eventDto.getLocation());
        event.setRegistrationDate(eventDto.getRegistrationDate());
        event.setMaxUserAllowed(eventDto.getMaxUserAllowed());
        event.setStart(eventDto.getStart());
        event.setEnd(eventDto.getEnd());
        event.setDescription(eventDto.getDescription());

        eventRepo.save(event);
        eventDto.setId(id);
        return eventDto;
    }

    @Override
    public EventDto getEvent(Long id) {
        Optional<Event> eventOptional = eventRepo.findById(id);
        if (eventOptional.isEmpty()) return null;
        Event event = eventOptional.get();
        return mapper.mapTo(event);
    }

    public List<EventDto> getAllEvents(int page, int size) {
        Sort sort = Sort.by("registrationDate").ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Event> events = eventRepo.findAll(pageable);
        List<EventDto> eventDtos = new ArrayList<>();
        for(Event event : events){
            eventDtos.add(mapper.mapTo(event));
        }
        return eventDtos;
    }

    public List<EventDto> getByKeyword(String keyword, int page, int size){
        Sort sort = Sort.by("name").ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Event> events = eventRepo.findByNameContaining(keyword, pageable);
        List<EventDto> eventDtos = new ArrayList<>();
        for(Event event : events){
            eventDtos.add(mapper.mapTo(event));
        }
        return eventDtos;
    }

    @Override
    public List<EventDto> getRegisteredEvents(int page, int size) {
        String email = authUtil.getEmailOfLoggedUser();
        Optional<User> userOptional = userService.getUserByEmail(email);
        if(userOptional.isEmpty()){
            throw new AccessDeniedException("Please logout and login again");
        }

        User user = userOptional.get();
        Sort sort = Sort.by("end").descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Event> events = eventRepo.findByRegisteredUserContaining(user, pageable);
        List<EventDto> eventDtos = new ArrayList<>();
        for(Event event : events){
            eventDtos.add(mapper.mapTo(event));
        }
        return eventDtos;
    }

    @Override
    public List<EventDto> getCreatedEvents(int page, int size) {
        Sort sort = Sort.by("start");
        Pageable pageable = PageRequest.of(page, size, sort);

        String email = authUtil.getEmailOfLoggedUser();
        Optional<User> userOptional = userService.getUserByEmail(email);
        if(userOptional.isEmpty()){
            throw new AccessDeniedException("Please logout and login again");
        }
        User user = userOptional.get();

        Page<Event> events = eventRepo.findByCreatedBy(user, pageable);
        List<EventDto> eventDtos = new ArrayList<>();
        for(Event event : events){
            eventDtos.add(mapper.mapTo(event));
        }
        return eventDtos;
    }

    @Override
    public EventDto registerInEvent(Long id) {
        String email = authUtil.getEmailOfLoggedUser();
        Optional<User> userOptional = userService.getUserByEmail(email);
        Optional<Event> eventOptional = eventRepo.findById(id);

        if(userOptional.isEmpty()){
            throw new AccessDeniedException("Please logout and login again");
        }

        if(eventOptional.isEmpty()){
            throw new AccessDeniedException("Please enter a valid event id");
        }

        Event event = eventOptional.get();
        User user = userOptional.get();

        user.getRegisteredEvents().add(event);
        event.getRegisteredUser().add(user);

        event.setUserRegistered((event.getUserRegistered()+1L));

        userService.saveUser(user);
        eventRepo.save(event);

        return mapper.mapTo(event);
    }
}
