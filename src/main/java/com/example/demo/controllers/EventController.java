package com.example.demo.controllers;

import com.example.demo.dtos.EventDto;
import com.example.demo.helper.AppConstants;
import com.example.demo.services.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event-admin")
@PreAuthorize("hasRole('EVENT_ADMIN')")
public class EventController {

    private EventService eventService;
    public EventController(EventService eventService){
        this.eventService = eventService;
    }

    @GetMapping("/home")
    public ResponseEntity<String> eventAdminHome(){
        return new ResponseEntity<>("event admin home", HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody EventDto eventDto){
        EventDto savedEventDto = eventService.createEvent(eventDto);
        return new ResponseEntity<>(savedEventDto, HttpStatus.CREATED);
    }

    @GetMapping("/get-created-events")
    public ResponseEntity<List<EventDto>> getCreatedEvents(
            @RequestParam(value = "page", defaultValue = AppConstants.page) int page,
            @RequestParam(value = "size", defaultValue = AppConstants.size) int size
    ){
        List<EventDto> list = eventService.getCreatedEvents(page, size);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EventDto> updateEvent(@Valid @RequestBody EventDto eventDto, @PathVariable Long id){
        EventDto updatedEventDto = eventService.updateEvent(id, eventDto);
        return new ResponseEntity<>(updatedEventDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteEvent(@RequestParam Long id){
        eventService.deleteEvent(id);
        return new ResponseEntity<>("Event deleted successfully", HttpStatus.NO_CONTENT);
    }
}
