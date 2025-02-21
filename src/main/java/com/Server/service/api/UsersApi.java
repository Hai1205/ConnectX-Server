package com.Server.service.api;

import com.Server.dto.*;
import com.Server.entity.*;
import com.Server.exception.OurException;
import com.Server.repo.*;
import com.Server.service.config.AwsS3Config;
import com.Server.utils.Utils;
import com.Server.utils.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import com.Server.dto.UserDTO;
import com.Server.entity.User;
import com.Server.repo.UserRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.web.multipart.MultipartFile;

@Service
public class UsersApi {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AwsS3Config awsS3Config;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Response getAllUsers(int page, int limit, String sort, String order) {
        Response response = new Response();

        try {
            Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, sort));

            Page<User> userPage = userRepository.findAll(pageable);
            List<UserDTO> userDTOList = UserMapper.mapListEntityToListDTOFull(userPage.getContent());

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setPagination(new Pagination(userPage.getTotalElements(), userPage.getTotalPages(), page));
            response.setUserList(userDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        }

        return response;
    }

    public Response getProfile(String username) {
        Response response = new Response();

        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new OurException("User Not Found"));
            UserDTO userDTO = UserMapper.mapEntityToDTOFull(user);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        }

        return response;
    }

    public Response getUserSuggested(String userId) {
        Response response = new Response();
        try {
            User currentUser = userRepository.findById(userId)
                    .orElseThrow(() -> new OurException("User Not Found"));

            List<String> followingIds = currentUser.getFollowingList()
                    .stream().map(User::get_id).toList();

            List<User> suggestedUsers = userRepository.findAll().stream()
                    .filter(user -> !user.get_id().equals(userId)) // Loại bỏ chính mình
                    .filter(user -> !followingIds.contains(user.get_id())) // Loại bỏ người đã follow
                    .toList();

            Collections.shuffle(suggestedUsers);
            List<User> randomUsers = suggestedUsers.stream()
                    .limit(10)
                    .toList();

            List<UserDTO> userDTOList = UserMapper.mapListEntityToListDTOLimit(randomUsers, 5);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUserList(userDTOList);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error");
            e.printStackTrace();
        }
        return response;
    }

    public Response followOrUnfollow(String currentUserId, String userToModifyId) {
        Response response = new Response();

        try {
            User currentUser = userRepository.findById(currentUserId).orElseThrow(() -> new OurException("User1 Not Found"));
            User userToModify = userRepository.findById(userToModifyId).orElseThrow(() -> new OurException("User Not Found"));

            if (userToModifyId.equals(currentUserId)) {
                response.setStatusCode(400);
                response.setMessage("You can't follow/unfollow yourself");

                return response;
            }

            boolean isFollowing = currentUser.getFollowingList()
                    .stream()
                    .map(dbRef -> dbRef.get_id().toString()) // Lấy ID từ DBRef
                    .anyMatch(id -> id.equals(userToModifyId)); // So sánh với ID của userToModify

            if (isFollowing) {
                currentUser.getFollowingList().removeIf(dbRef -> dbRef.get_id().toString().equals(userToModifyId));
                userToModify.getFollowerList().removeIf(dbRef -> dbRef.get_id().toString().equals(currentUserId));

                response.setMessage("User unfollowed successfully");
            } else {
                currentUser.getFollowingList().add(userToModify);
                userToModify.getFollowerList().add(currentUser);

                Notification notification = new Notification("follow", currentUser, userToModify);
                notificationRepository.save(notification);

                response.setMessage("User followed successfully");
            }

            userRepository.save(currentUser);
            userRepository.save(userToModify);

            response.setStatusCode(200);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        }

        return response;
    }

    public Response updateUser(String formData, MultipartFile profileImg, MultipartFile coverImg, String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            if (formData != null) {
                Request formDataRequest = Utils.parseStringToJSonData(formData);

                if (formDataRequest == null) {
                    response.setStatusCode(403);
                    response.setMessage("Invalid JSON format");

                    return response;
                }

                String fullName = formDataRequest.getFullName();
                String newPassword = formDataRequest.getNewPassword();
                String confirmPassword = formDataRequest.getConfirmPassword();
                String currentPassword = formDataRequest.getCurrentPassword();
                String bio = formDataRequest.getBio();
                String link = formDataRequest.getLink();

                if (fullName != null && !fullName.isEmpty()) {
                    user.setFullName(fullName);
                }

                if (currentPassword != null && !currentPassword.isEmpty() &&
                        newPassword != null && !newPassword.isEmpty() &&
                        confirmPassword != null && !confirmPassword.isEmpty()
                ) {
                    boolean isMatch = passwordEncoder.matches(currentPassword, user.getPassword());

                    if (!isMatch) {
                        response.setStatusCode(400);
                        response.setMessage("Current password is incorrect");

                        return response;
                    }

                    if (!newPassword.equals(confirmPassword)) {
                        response.setStatusCode(400);
                        response.setMessage("Current password does not match");

                        return response;
                    }

                    user.setPassword(passwordEncoder.encode(newPassword));
                }

                if (bio != null && !bio.isEmpty()) {
                    user.setBio(bio);
                }

                if (link != null && !link.isEmpty()) {
                    user.setLink(link);
                }
            }

            if (profileImg != null && !profileImg.isEmpty()) {
                System.out.println();
                String profileImgUrl = user.getProfileImgUrl();
                String defaultProfileImgUrl = "https://connect-x-server.s3.ap-southeast-1.amazonaws.com/avatar-placeholder.png";
                if (profileImgUrl != null &&
                        !profileImgUrl.isEmpty() &&
                        !profileImgUrl.equals(defaultProfileImgUrl)
                ) awsS3Config.deleteImageFromS3(profileImgUrl);

                profileImgUrl = awsS3Config.saveImageToS3(profileImg);
                user.setProfileImgUrl(profileImgUrl);
            }

            if (coverImg != null && !coverImg.isEmpty()) {
                String coverImgUrl = user.getCoverImgUrl();
                String defaultCoverImgUrl = "https://connect-x-server.s3.ap-southeast-1.amazonaws.com/cover.png";
                if (coverImgUrl != null &&
                        !coverImgUrl.isEmpty() &&
                        !coverImgUrl.equals(defaultCoverImgUrl)
                ) awsS3Config.deleteImageFromS3(coverImgUrl);

                coverImgUrl = awsS3Config.saveImageToS3(coverImg);
                user.setCoverImgUrl(coverImgUrl);
            }

            User savedUser = userRepository.save(user);
            UserDTO userDTO = UserMapper.mapEntityToDTOFull(savedUser);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        }

        return response;
    }

    public Response deleteUser(String userId) {
        Response response = new Response();

        try {
            userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            userRepository.deleteById(userId);

            response.setStatusCode(200);
            response.setMessage("successful");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        }

        return response;
    }
}
