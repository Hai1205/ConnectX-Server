package com.Server.utils.mapper;

import com.Server.dto.UserDTO;
import com.Server.entity.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO mapEntityToDTOFull(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.set_id(user.get_id());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullName(user.getFullName());
        userDTO.setProfileImgUrl(user.getProfileImgUrl());
        userDTO.setCoverImgUrl(user.getCoverImgUrl());
        userDTO.setBio(user.getBio());
        userDTO.setLink(user.getLink());

        userDTO.setPostList(PostMapper.mapListEntityToListDTO(
                user.getPostList().stream()
                        .filter(Objects::nonNull)
                        .toList()
        ));
        userDTO.setFollowerList(mapListToDTO(
                user.getFollowerList().stream()
                        .filter(Objects::nonNull)
                        .toList()
        ));
        userDTO.setFollowingList(mapListToDTO(
                user.getFollowingList().stream()
                        .filter(Objects::nonNull)
                        .toList()
        ));
        userDTO.setLikedPostList(PostMapper.mapListEntityToListDTO(
                user.getLikedPostList().stream()
                        .filter(Objects::nonNull)
                        .toList()
        ));
        userDTO.setSharedPostList(PostMapper.mapListEntityToListDTO(
                user.getSharedPostList().stream()
                        .filter(Objects::nonNull)
                        .toList()
        ));
        userDTO.setBookmarkedPostList(PostMapper.mapListEntityToListDTO(
                user.getBookmarkedPostList().stream()
                        .filter(Objects::nonNull)
                        .toList()
        ));

        userDTO.setCreatedAt(user.getCreatedAt());

        return userDTO;
    }

    private static List<UserDTO> mapListToDTO(List<User> users) {
        return users.stream().map(user -> new UserDTO(
                user.get_id(),
                user.getUsername(),
                user.getFullName(),
                user.getProfileImgUrl()
        )).collect(Collectors.toList());
    }

    public static UserDTO mapEntityToDTO(User user) {
        return new UserDTO(
                user.get_id(),
                user.getUsername(),
                user.getFullName(),
                user.getProfileImgUrl());
    }

    public static List<UserDTO> mapListEntityToListDTO(List<User> userList) {
        return userList.stream()
                .map(UserMapper::mapEntityToDTO)
                .collect(Collectors.toList());
    }

    public static List<UserDTO> mapListEntityToListDTOFull(List<User> userList) {
        return userList.stream()
                .map(UserMapper::mapEntityToDTOFull)
                .collect(Collectors.toList());
    }

    public static List<UserDTO> mapListEntityToListDTOLimit(List<User> userList, int limit) {
        return userList.stream()
                .limit(limit)
                .map(UserMapper::mapEntityToDTO)
                .collect(Collectors.toList());
    }
}
