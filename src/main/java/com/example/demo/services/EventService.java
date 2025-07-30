package com.example.demo.services;

import com.example.demo.dtos.EventDto;

import java.util.List;

public interface EventService {
    EventDto createEvent(EventDto eventDto);
    void deleteEvent(Long id);

    EventDto updateEvent(Long id, EventDto eventDto);

    EventDto getEvent(Long id);

    List<EventDto> getAllEvents(int page, int size);

    List<EventDto> getByKeyword(String keyword, int page, int size);

    List<EventDto> getRegisteredEvents(int page, int size);

    List<EventDto> getCreatedEvents(int page, int size);

    EventDto registerInEvent(Long id);
}
