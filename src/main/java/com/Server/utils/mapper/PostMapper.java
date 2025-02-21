package com.Server.utils.mapper;

import com.Server.dto.PostDTO;
import com.Server.entity.Post;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PostMapper {
    public static PostDTO mapEntityToDTOFull(Post post) {
        PostDTO postDTO = new PostDTO();

        System.out.println(post.get_id());
        postDTO.set_id(post.get_id());
        postDTO.setUser(UserMapper.mapEntityToDTO(post.getUser()));
        postDTO.setContent(post.getContent());

        postDTO.setImageUrlList(post.getImageUrlList());
        postDTO.setBookmarkList(UserMapper.mapListEntityToListDTO(
                post.getBookmarkList().stream()
                        .filter(Objects::nonNull)
                        .toList()
        ));
        postDTO.setShareList(UserMapper.mapListEntityToListDTO(
                post.getShareList().stream()
                        .filter(Objects::nonNull)
                        .toList()
        ));
        postDTO.setLikeList(UserMapper.mapListEntityToListDTO(
                post.getLikeList().stream()
                        .filter(Objects::nonNull)
                        .toList()
        ));
        postDTO.setCommentList(CommentMapper.mapListEntityToListDTO(
                post.getCommentList().stream()
                        .filter(Objects::nonNull)
                        .toList()
        ));

        postDTO.setCreatedAt(post.getCreatedAt());

        return postDTO;
    }

    public static List<PostDTO> mapListEntityToListDTO(List<Post> postList) {
        return postList.stream()
                .map(PostMapper::mapEntityToDTOFull)
                .collect(Collectors.toList());
    }

    public static PostDTO mapEntityToDTO(Post post) {
        return new PostDTO(
                post.get_id());
    }
}
