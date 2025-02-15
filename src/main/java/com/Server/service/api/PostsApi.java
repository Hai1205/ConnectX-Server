package com.Server.service.api;

import com.Server.dto.*;
import com.Server.entity.*;
import com.Server.exception.OurException;
import com.Server.repo.*;
import com.Server.service.AwsS3Service;
import com.Server.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PostsApi {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private AwsS3Service awsS3Service;

    public Response createPost(List<MultipartFile> photos, String text, String userId) {
        Response response = new Response();

        try {
            Post formDataPost = new Post();

            userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            formDataPost.setUserId(userId);

            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile photo : photos) {
                String imageUrl = awsS3Service.saveImageToS3(photo);
                imageUrls.add(imageUrl);
            }
            formDataPost.setImageList(imageUrls);

            formDataPost.setText(text);

            Post post = postRepository.save(formDataPost);
            PostDTO postDTO = Utils.mapPostEntityToPostDTO(post);

            response.setStatusCode(200);
            response.setMessage("success");
            response.setPostDTO(postDTO);
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

    public Response deletePost(String postId) {
        Response response = new Response();

        try {
            Post post = postRepository.findById(postId).orElseThrow(() -> new OurException("Post Not Found"));

            List<String> imageUrls = post.getImageList();
            for (String imageUrl : imageUrls) {
                awsS3Service.deleteImageFromS3(imageUrl);
            }

            postRepository.deleteById(postId);

            response.setStatusCode(200);
            response.setMessage("success");
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

    public Response likePost(String userId, String postId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            Post post = postRepository.findById(postId).orElseThrow(() -> new OurException("Post Not Found"));

            boolean isLike = post.getLikeList().contains(userId);
            if (isLike) {
                post.getLikeList().remove(userId);
                user.getLikedList().remove(postId);

                response.setMessage("Post unliked successfully");
            } else {
                post.getLikeList().add(userId);
                Notification notification = new Notification("like", userId, post.getUserId());
                notificationRepository.save(notification);

                user.getLikedList().add(postId);

                response.setMessage("Post liked successfully");
            }

            userRepository.save(user);
            postRepository.save(post);

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

    public Response getAllPosts(int page, int limit, String sort, String order) {
        Response response = new Response();

        try {
            Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, sort));

            Page<Post> postPage = postRepository.findAll(pageable);
            List<PostDTO> postDTOList = Utils.mapPostListEntityToPostListDTO(postPage.getContent());

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setPagination(new Pagination(postPage.getTotalElements(), postPage.getTotalPages(), page));
            response.setPostDTOList(postDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        }

        return response;
    }

    public Response likedPosts(int page, int limit, String sort, String order, String userId) {
        Response response = new Response();

        try {
            Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, sort));

            Page<Post> postPage = postRepository.findByUserId(pageable, userId);
            List<PostDTO> postDTOList = Utils.mapPostListEntityToPostListDTO(postPage.getContent());

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setPagination(new Pagination(postPage.getTotalElements(), postPage.getTotalPages(), page));
            response.setPostDTOList(postDTOList);
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

    public Response followingPosts(String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            List<String> followingPosts = user.getFollowingList();

            if (followingPosts.isEmpty()) {
                response.setStatusCode(200);
                response.setMessage("User is not following anyone");
                response.setPostDTOList(new ArrayList<>());

                return response;
            }

            List<Post> feedPosts = postRepository.findByUserIdIn(followingPosts);
            List<PostDTO> postDTOList = Utils.mapPostListEntityToPostListDTO(feedPosts);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setPostDTOList(postDTOList);
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

    public Response userPosts(String userId) {
        Response response = new Response();

        try {
            userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            List<Post> userPosts = postRepository.findByUserId(userId);
            List<PostDTO> postDTOList = Utils.mapPostListEntityToPostListDTO(userPosts);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setPostDTOList(postDTOList);
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
