package com.mustafaz.telemed.notifications.service;

import com.mustafaz.telemed.notifications.dto.NotificationDTO;
import com.mustafaz.telemed.users.entity.User;


public interface NotificationService {
    void sendEmail(NotificationDTO notificationDTO, User user);
}
