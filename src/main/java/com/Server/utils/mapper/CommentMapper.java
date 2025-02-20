package com.Server.utils.mapper;

import com.Server.dto.CommentDTO;
import com.Server.entity.Comment;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static CommentDTO mapEntityToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.set_id(comment.get_id());
        commentDTO.setPost(PostMapper.mapEntityToDTO(comment.getPost()));
        commentDTO.setUser(UserMapper.mapEntityToDTO(comment.getUser()));
        commentDTO.setContent(comment.getContent());
        commentDTO.setImgUrl(comment.getImgUrl());
        commentDTO.setCreatedAt(comment.getCreatedAt());

        return commentDTO;
    }

    public static List<CommentDTO> mapListEntityToListDTO(List<Comment> commentList) {
        return commentList.stream().map(CommentMapper::mapEntityToDTO).collect(Collectors.toList());
    }
}
