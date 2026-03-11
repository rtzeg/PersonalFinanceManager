package com.pfm.notification.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "notifications")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Long userId;
    private String title;
    @Column(columnDefinition = "text")
    private String message;
    private String type;
    private boolean read = false;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
