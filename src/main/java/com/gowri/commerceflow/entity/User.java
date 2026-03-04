package com.gowri.commerceflow.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity // connecting JPA with DB
@Table(name = "users") //table name
@Getter
@Setter
//creating constructor
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id //Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    //Storing as String instead of 0, 1
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    //Calling every time before inserting
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
