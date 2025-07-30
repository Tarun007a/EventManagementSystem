package com.example.demo.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

@Data
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Size(min = 2, message = "name should be of atleast 2 character")
    private String name;

    @Size(min = 8, message = "password should be of atleast 8 character")
    private String password;

    @Email(message = "enter a valid email address")
    @NotBlank(message = "email is required")
    @Column(unique = true, nullable = false)
    private String email;

    @NotBlank(message = "phone number is required")
    @Size(min = 10, max = 12, message = "phone number should be of atleast 10 and max 12 digits")
    private String phoneNumber;
}
