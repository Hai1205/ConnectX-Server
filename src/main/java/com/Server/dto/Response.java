package com.Server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {
    public Response(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public Response() {
    }

    private int statusCode;
    private String message;
    private Pagination pagination;

    private String token;
    private String role;
    private String expirationTime;

    private UserDTO user;
    private NotificationDTO notificationDTO;
    private PostDTO postDTO;
    private CommentDTO commentDTO;

    private List<UserDTO> userList;
    private List<NotificationDTO> notificationDTOList;
    private List<PostDTO> postDTOList;
    private List<CommentDTO> commentDTOList;
}
