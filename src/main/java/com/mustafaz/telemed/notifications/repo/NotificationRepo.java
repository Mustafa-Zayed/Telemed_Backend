package com.mustafaz.telemed.notifications.repo;

import com.mustafaz.telemed.notifications.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<Notification, Long> {
}