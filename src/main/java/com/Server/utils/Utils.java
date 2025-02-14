package com.Server.utils;

import com.Server.dto.*;
import com.Server.entity.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private static final SecureRandom secureRandom = new SecureRandom();

    public static String generateRandomConfirmationCode(int length) {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }

        return stringBuilder.toString();
    }

    public static UserDTO mapUserEntityToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.set_id(user.get_id());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullName(user.getFullName());
        userDTO.setFollowers(user.getFollowers());
        userDTO.setFollowing(user.getFollowing());
        userDTO.setProfileImg(user.getProfileImg());
        userDTO.setCoverImg(user.getCoverImg());
        userDTO.setBio(user.getBio());
        userDTO.setLink(user.getLink());
        userDTO.setCreatedAt(user.getCreatedAt());

        return userDTO;
    }

    public static NotificationDTO mapNotificationEntityToNotificationDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.set_id(notification.get_id());
        notificationDTO.setCreatedAt(notification.getCreatedAt());

        return notificationDTO;
    }

    public static PostDTO mapBookingEntityToBookingDTO(Post post) {
        PostDTO postDTO = new PostDTO();

        postDTO.set_id(post.get_id());
        postDTO.setCreatedAt(post.getCreatedAt());

        return postDTO;
    }

    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList) {
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }

    public static List<NotificationDTO> mapNotificationListEntityToNotificationListDTO(List<Notification> notificationList) {
        return notificationList.stream().map(Utils::mapNotificationEntityToNotificationDTO).collect(Collectors.toList());
    }

    public static List<PostDTO> mapPostListEntityToPostListDTO(List<Post> postList) {
        return postList.stream().map(Utils::mapBookingEntityToBookingDTO).collect(Collectors.toList());
    }
}
