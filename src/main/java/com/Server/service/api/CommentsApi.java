package com.Server.service.api;

import com.Server.dto.CommentDTO;
import com.Server.dto.Pagination;
import com.Server.dto.Response;
import com.Server.entity.Comment;
import com.Server.entity.Notification;
import com.Server.entity.Post;
import com.Server.exception.OurException;
import com.Server.repo.CommentRepository;
import com.Server.repo.NotificationRepository;
import com.Server.repo.PostRepository;
import com.Server.repo.UserRepository;
import com.Server.service.AwsS3Service;
import com.Server.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentsApi {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AwsS3Service awsS3Service;

    @Autowired
    private NotificationRepository notificationRepository;

    public Response getAllComments(int page, int limit, String sort, String order) {
        Response response = new Response();

        try {
            Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, sort));

            Page<Comment> commentPage = commentRepository.findAll(pageable);
            List<CommentDTO> commentDTOList = Utils.mapCommentListEntityToCommentListDTO(commentPage.getContent());

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setPagination(new Pagination(commentPage.getTotalElements(), commentPage.getTotalPages(), page));
            response.setCommentDTOList(commentDTOList);

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

    public Response deleteComment(String commentId) {
        Response response = new Response();

        try {
            commentRepository.findById(commentId).orElseThrow(() -> new OurException("Comment Not Found"));
            commentRepository.deleteById(commentId);

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

    public Response postComments(String postId) {
        Response response = new Response();

        try {
            postRepository.findById(postId).orElseThrow(() -> new OurException("Post Not Found"));

            List<Comment> commentPosts = commentRepository.findByPostId(postId);
            List<CommentDTO> commentsDTOList = Utils.mapCommentListEntityToCommentListDTO(commentPosts);

            response.setStatusCode(200);
            response.setMessage("success");
            response.setCommentDTOList(commentsDTOList);
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

    public Response deletePostComment(String postId) {
        Response response = new Response();

        try {
            Post post = postRepository.findById(postId).orElseThrow(() -> new OurException("Post Not Found"));

            List<String> postComments = post.getCommentList();
            for (String commentId : postComments) {
                deleteComment(commentId);
                post.getCommentList().remove(commentId);
            }
            postRepository.save(post);

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

    public Response updateComments(String commentId, String formData, MultipartFile img) {
        Response response = new Response();

        try {
            Comment formDataComment = parseCommentData(formData);
            if (formDataComment == null) {
                response.setStatusCode(403);
                response.setMessage("Invalid JSON format");

                return response;
            }

            String postId = formDataComment.getPostId();
            String userId = formDataComment.getUserId();
            String text = formDataComment.getText();

            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new OurException("Comment Not Found"));
            userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            postRepository.findById(postId).orElseThrow(() -> new OurException("Post Not Found"));

            if (text == null && text.isEmpty() && img == null) {
                response.setStatusCode(400);
                response.setMessage("Text field is empty");

                return response;
            }

            if (img != null) {
                String imageUrl = awsS3Service.saveImageToS3(img);
                comment.setImg(imageUrl);
            }

            comment.setText(text);

            Comment savedComment = commentRepository.save(comment);
            CommentDTO commentDTO = Utils.mapCommentEntityToCommentDTO(savedComment);

            response.setStatusCode(200);
            response.setMessage("success");
            response.setCommentDTO(commentDTO);
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

    public Response createComments(String formData, MultipartFile img) {
        Response response = new Response();

        try {
            Comment formDataComment = parseCommentData(formData);
            if (formDataComment == null) {
                response.setStatusCode(403);
                response.setMessage("Invalid JSON format");

                return response;
            }

            String postId = formDataComment.getPostId();
            String userId = formDataComment.getUserId();
            String text = formDataComment.getText();

            userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            Post post = postRepository.findById(postId).orElseThrow(() -> new OurException("Post Not Found"));

            if (text == null && text.isEmpty() && img == null) {
                response.setStatusCode(400);
                response.setMessage("Text field is empty");

                return response;
            }

            if (img != null) {
                String imageUrl = awsS3Service.saveImageToS3(img);
                formDataComment.setImg(imageUrl);
            }

            formDataComment.setUserId(userId);
            formDataComment.setPostId(postId);
            formDataComment.setText(text);

            Comment comment = commentRepository.save(formDataComment);
            CommentDTO commentDTO = Utils.mapCommentEntityToCommentDTO(comment);

            post.getCommentList().add(comment.get_id());
            postRepository.save(post);

            Notification notification = new Notification("comment", userId, post.getUserId());
            notificationRepository.save(notification);

            response.setStatusCode(200);
            response.setMessage("success");
            response.setCommentDTO(commentDTO);
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

    private Comment parseCommentData(String formData) {
        try {
            return new ObjectMapper().readValue(formData, Comment.class);
        } catch (Exception e) {
            return null;
        }
    }
}
