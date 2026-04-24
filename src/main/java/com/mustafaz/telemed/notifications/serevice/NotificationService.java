package com.mustafaz.telemed.notifications.serevice;

import com.mustafaz.telemed.notifications.dto.NotificationDTO;
import com.mustafaz.telemed.users.entity.User;


public interface NotificationService {
    void sendEmail(NotificationDTO notificationDTO, User user);
}
