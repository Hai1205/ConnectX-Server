package com.Server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    private int statusCode;
    private String message;
    private Pagination pagination;

    private String token;
    private String role;
    private String expirationTime;
    private String bookingConfirmationCode;

    private UserDTO user;
    private NotificationDTO room;
    private PostDTO booking;

    private List<UserDTO> userList;
    private List<NotificationDTO> roomList;
    private List<PostDTO> bookingList;
}
