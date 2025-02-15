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
        userDTO.setFollowerList(user.getFollowerList());
        userDTO.setFollowingList(user.getFollowingList());
        userDTO.setProfileImg(user.getProfileImg());
        userDTO.setCoverImg(user.getCoverImg());
        userDTO.setBio(user.getBio());
        userDTO.setLink(user.getLink());
        userDTO.setLikedList(user.getLikedList());
        userDTO.setCreatedAt(user.getCreatedAt());

        return userDTO;
    }

    public static NotificationDTO mapNotificationEntityToNotificationDTO(Notification notification) {
        NotificationDTO notificationDTO = new NotificationDTO();

        notificationDTO.set_id(notification.get_id());
        notificationDTO.setFrom(notification.getFrom());
        notificationDTO.setTo(notification.getTo());
        notificationDTO.setType(notification.getType());
        notificationDTO.setRead(notification.isRead());
        notificationDTO.setCreatedAt(notification.getCreatedAt());

        return notificationDTO;
    }

    public static PostDTO mapPostEntityToPostDTO(Post post) {
        PostDTO postDTO = new PostDTO();

        postDTO.set_id(post.get_id());
        postDTO.setUserId(post.getUserId());
        postDTO.setText(post.getText());
        postDTO.setImageList(post.getImageList());
        postDTO.setLikeList(post.getLikeList());
        postDTO.setCommentList(post.getCommentList());
        postDTO.setCreatedAt(post.getCreatedAt());

        return postDTO;
    }

    public static CommentDTO mapCommentEntityToCommentDTO(Comment post) {
        CommentDTO commentDTO = new CommentDTO();

        commentDTO.set_id(post.get_id());
        commentDTO.setUserId(post.getUserId());
        commentDTO.setPostId(post.getPostId());
        commentDTO.setText(post.getText());
        commentDTO.setImg(post.getImg());
        commentDTO.setCreatedAt(post.getCreatedAt());

        return commentDTO;
    }

    public static List<UserDTO> mapUserListEntityToUserListDTO(List<User> userList) {
        return userList.stream().map(Utils::mapUserEntityToUserDTO).collect(Collectors.toList());
    }

    public static List<NotificationDTO> mapNotificationListEntityToNotificationListDTO(List<Notification> notificationList) {
        return notificationList.stream().map(Utils::mapNotificationEntityToNotificationDTO).collect(Collectors.toList());
    }

    public static List<PostDTO> mapPostListEntityToPostListDTO(List<Post> postList) {
        return postList.stream().map(Utils::mapPostEntityToPostDTO).collect(Collectors.toList());
    }

    public static List<CommentDTO> mapCommentListEntityToCommentListDTO(List<Comment> commentList) {
        return commentList.stream().map(Utils::mapCommentEntityToCommentDTO).collect(Collectors.toList());
    }
}
