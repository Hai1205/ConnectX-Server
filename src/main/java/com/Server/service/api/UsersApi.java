package com.Server.service.api;

import com.Server.dto.*;
import com.Server.entity.*;
import com.Server.exception.OurException;
import com.Server.repo.*;
import com.Server.service.AwsS3Service;
import com.Server.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import com.Server.dto.UserDTO;
import com.Server.entity.User;
import com.Server.repo.UserRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;
import java.util.Random;;

@Service
public class UsersApi {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AwsS3Service awsS3Service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Response getAllUsers(int page, int limit, String sort, String order) {
        Response response = new Response();

        try {
            Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, sort));

            Page<User> userPage = userRepository.findAll(pageable);
            List<UserDTO> userDTOList = Utils.mapUserListEntityToUserListDTO(userPage.getContent());

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

    public Response profile(String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);

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

    public Response suggested(String userId) {
        Response response = new Response();
        try {
            User usersFollowedByMe = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            // Lấy danh sách người dùng khác ngoài người dùng hiện tại
            List<User> allUsers = userRepository.findAll();
            List<User> filteredUsers = allUsers.stream()
                    .filter(user -> !user.get_id().equals(userId))
                    .filter(user -> !usersFollowedByMe.getFollowingList().contains(user.get_id()))
                    .toList();

            // Chọn ngẫu nhiên 10 người dùng
            Random random = new Random();
            List<User> randomUsers = filteredUsers.stream()
                    .skip(random.nextInt(filteredUsers.size())) // Chọn một phần ngẫu nhiên
                    .limit(10)
                    .toList();

            // Chỉ lấy 4 người dùng đầu tiên
            List<UserDTO> suggestedUsers = randomUsers.stream()
                    .limit(4)
                    .map(Utils::mapUserEntityToUserDTO) // Chuyển đổi sang DTO
                    .collect(Collectors.toList());

            // Không trả mật khẩu của người dùng
            suggestedUsers.forEach(user -> user.setPassword(null));

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUserList(suggestedUsers);
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

    public Response followOrUnfollow(String currentUserId, String userToModifyId) {
        Response response = new Response();

        try {
            User userToModify = userRepository.findById(userToModifyId).orElseThrow(() -> new OurException("User Not Found"));
            User currentUser = userRepository.findById(currentUserId).orElseThrow(() -> new OurException("User Not Found"));

            if (userToModifyId.equals(currentUserId)) {
                response.setStatusCode(400);
                response.setMessage("You can't follow/unfollow yourself");

                return response;
            }

            boolean isFollowing = currentUser.getFollowingList().contains(userToModifyId);
            if (isFollowing) {
                currentUser.getFollowingList().remove(userToModifyId);
                userToModify.getFollowerList().remove(currentUserId);

//                Notification notification = new Notification("unfollow", currentUserId, userToModifyId);
//                notificationRepository.save(notification);

                response.setMessage("User unfollowed successfully");
            } else {
                currentUser.getFollowingList().add(userToModifyId);
                userToModify.getFollowerList().add(currentUserId);

                Notification notification = new Notification("follow", currentUserId, userToModifyId);
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
            User formDataUser = parseUserData(formData);
            if (formDataUser == null) {
                response.setStatusCode(403);
                response.setMessage("Invalid JSON format");

                return response;
            }

            String fullName = formDataUser.getFullName();
            String newPassword = formDataUser.getNewPassword();
            String confirmPassword = formDataUser.getConfirmPassword();
            String currentPassword = formDataUser.getCurrentPassword();
            String bio = formDataUser.getBio();
            String link = formDataUser.getLink();

            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

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

            if (profileImg != null && !profileImg.isEmpty()) {
                String profileImgUrl = user.getProfileImg();
                if (profileImgUrl != null && !profileImgUrl.isEmpty()) awsS3Service.deleteImageFromS3(profileImgUrl);

                profileImgUrl = awsS3Service.saveImageToS3(profileImg);
                user.setProfileImg(profileImgUrl);
            }

            if (coverImg != null && !coverImg.isEmpty()) {
                String coverImgUrl = user.getCoverImg();
                if (coverImgUrl != null && !coverImgUrl.isEmpty()) awsS3Service.deleteImageFromS3(coverImgUrl);

                coverImgUrl = awsS3Service.saveImageToS3(coverImg);
                user.setCoverImg(coverImgUrl);
            }

            User savedUser = userRepository.save(user);
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(savedUser);

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

    private User parseUserData(String formData) {
        try {
            return new ObjectMapper().readValue(formData, User.class);
        } catch (Exception e) {
            return null;
        }
    }
}
