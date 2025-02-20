package com.Server.utils.mapper;

import com.Server.dto.PostDTO;
import com.Server.dto.UserDTO;
import com.Server.entity.Post;
import com.Server.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {
    public static PostDTO mapEntityToDTOFull(Post post) {
        PostDTO postDTO = new PostDTO();

        postDTO.set_id(post.get_id());
        postDTO.setUser(UserMapper.mapEntityToDTO(post.getUser()));
        postDTO.setContent(post.getContent());

        postDTO.setImageUrlList(post.getImageUrlList());
        postDTO.setBookmarkList(UserMapper.mapListEntityToListDTO(post.getBookmarkList()));
        postDTO.setShareList(UserMapper.mapListEntityToListDTO(post.getShareList()));
        postDTO.setLikeList(UserMapper.mapListEntityToListDTO(post.getLikeList()));
        postDTO.setCommentList(CommentMapper.mapListEntityToListDTO(post.getCommentList()));

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
