package com.Server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificationDTO {
    private String _id;

    private String from;

    private String to;

    private String type;

    private boolean read;

    private Instant createdAt;
}
