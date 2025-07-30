package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class EventDto {
    private long id;

    @NotBlank(message = "Event name is required")
    private String name;

    @NotBlank(message = "location is required it can be remote or any place")
    private String location;

    private long userRegistered;

    private long maxUserAllowed;

    @NotNull(message = "Registration end date is required")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDate;

    @NotNull(message = "Event date is required")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start;

    @NotNull(message = "Event end date is required")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end;

    @Size(min = 50, message = "Event description should contain at least 50 characters")
    private String description;
}





































