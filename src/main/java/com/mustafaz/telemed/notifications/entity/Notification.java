package com.mustafaz.telemed.notifications.entity;


import com.mustafaz.telemed.enums.NotificationType;
import com.mustafaz.telemed.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private String recipient;
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationType type; // EMAIL, SMS, PUSH

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private final LocalDateTime createdAt = LocalDateTime.now();
}