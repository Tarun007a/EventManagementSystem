package com.example.demo.repository;

import com.example.demo.entities.Event;
import com.example.demo.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepo extends JpaRepository<Event, Long> {
    Page<Event> findAll(Pageable pageable);

    Page<Event> findByNameContaining(String name, Pageable pageable);

    Page<Event> findByRegisteredUserContaining(User user, Pageable pageable);

    Page<Event> findByCreatedBy(User user, Pageable pageable);
}
