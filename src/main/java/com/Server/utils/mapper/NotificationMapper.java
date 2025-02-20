package com.Server.utils.mapper;

import com.Server.dto.NotificationDTO;
import com.Server.entity.Notification;

import java.util.List;
import java.util.stream.Collectors;

public class NotificationMapper {
    public static NotificationDTO mapEntityToDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.set_id(notification.get_id());
        notificationDTO.setFrom(UserMapper.mapEntityToDTO(notification.getFrom()));
        notificationDTO.setTo(UserMapper.mapEntityToDTO(notification.getTo()));
        notificationDTO.setType(notification.getType());
        notificationDTO.setRead(notification.isRead());
        notificationDTO.setCreatedAt(notification.getCreatedAt());

        return notificationDTO;
    }

    public static List<NotificationDTO> mapListEntityToListDTO(List<Notification> notificationList) {
        return notificationList.stream().map(NotificationMapper::mapEntityToDTO).collect(Collectors.toList());
    }
}
