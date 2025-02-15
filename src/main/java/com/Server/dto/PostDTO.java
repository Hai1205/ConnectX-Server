package com.Server.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostDTO {
    private String _id;

    private String userId;

    private String text;

    private List<String> imageList = new ArrayList<>();

    private List<String> likeList = new ArrayList<>();

    private List<String> commentList = new ArrayList<>();

    private Instant createdAt;
}
