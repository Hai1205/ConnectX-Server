package com.Server.service.api;

import com.Server.dto.*;
import com.Server.entity.*;
import com.Server.exception.OurException;
import com.Server.repo.*;
import com.Server.utils.JWTUtils;
import com.Server.utils.Utils;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UsersApi {
    @Autowired
    private UserRepository userRepository;

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
        }

        return response;
    }

    public Response profile(String username) {
        Response response = new Response();

        try {
            User user = userRepository.findByUsername(username).orElseThrow(()-> new OurException("User Not Found"));
            UserDTO userDTO = Utils.mapUserEntityToUserDTO(user);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setUser(userDTO);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    public Response suggested() {
        Response response = new Response();

        try {


            response.setStatusCode(200);
            response.setMessage("successful");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    public Response followOrUnfollow(String currentUserId, String userToModifyId) {
        Response response = new Response();

        try {
            User userToModify = userRepository.findById(userToModifyId).orElseThrow(()-> new OurException("User Not Found"));
            User currentUser = userRepository.findById(currentUserId).orElseThrow(()-> new OurException("User Not Found"));

            if(userToModifyId.equals(currentUserId)){
                response.setStatusCode(400);
                response.setMessage("You can't follow/unfollow yourself");

                return response;
            }

            boolean isFollowing = currentUser.getFollowing().contains(userToModify.get_id());
            if (isFollowing) {
                currentUser.getFollowing().remove(userToModify.get_id());
                userToModify.getFollowers().remove(currentUser.get_id());

                response.setMessage("User unfollowed successfully");
            } else {
                currentUser.getFollowing().add(userToModify.get_id());
                userToModify.getFollowers().add(currentUser.get_id());

                response.setMessage("User followed successfully");
            }

            userRepository.save(currentUser);
            userRepository.save(userToModify);

            response.setStatusCode(200);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    public Response updateUser(String userId) {
        Response response = new Response();

        try {


            response.setStatusCode(200);
            response.setMessage("successful");
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
        }

        return response;
    }
}
//1h20m50