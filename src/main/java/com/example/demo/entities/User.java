package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class User implements UserDetails {
    @Id
    private String userId;

    private String name;

    private String password;

    private String email;

    private String phoneNumber;

    private boolean enabled = true;
    private boolean emailVerified = true;
    private boolean phoneVerified = true;
    private boolean credentialNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean accountNonExpired = true;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<SimpleGrantedAuthority> roles =
                roleList.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
        return roles;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isEnabled(){
        return this.enabled;
    }

    @Override
    public boolean isAccountNonExpired(){
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked(){
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return this.credentialNonExpired;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_event",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    List<Event> registeredEvents = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    List<Event> createdEvent = new ArrayList<>();
}
