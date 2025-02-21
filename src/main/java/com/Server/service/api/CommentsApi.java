package com.Server.service.api;

import com.Server.dto.CommentDTO;
import com.Server.dto.Pagination;
import com.Server.dto.Response;
import com.Server.entity.Comment;
import com.Server.entity.Notification;
import com.Server.entity.Post;
import com.Server.entity.User;
import com.Server.exception.OurException;
import com.Server.repo.CommentRepository;
import com.Server.repo.NotificationRepository;
import com.Server.repo.PostRepository;
import com.Server.repo.UserRepository;
import com.Server.service.config.AwsS3Config;
import com.Server.utils.mapper.CommentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private AwsS3Config awsS3Config;

    @Autowired
    private NotificationRepository notificationRepository;

    public Response getAllComments(int page, int limit, String sort, String order) {
        Response response = new Response();

        try {
            Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(direction, sort));

            Page<Comment> commentPage = commentRepository.findAll(pageable);
            List<CommentDTO> commentDTOList = CommentMapper.mapListEntityToListDTO(commentPage.getContent());

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setPagination(new Pagination(commentPage.getTotalElements(), commentPage.getTotalPages(), page));
            response.setCommentDTOList(commentDTOList);
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

    public Response getUserComments(String postId) {
        Response response = new Response();

        try {
            Post post = postRepository.findById(postId).orElseThrow(() -> new OurException("Post Not Found"));

            List<Comment> commentPosts = commentRepository.findByPost(post);
            List<CommentDTO> commentsDTOList = CommentMapper.mapListEntityToListDTO(commentPosts);

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

            List<Comment> postComments = post.getCommentList();
            for (Comment comment : postComments) {
                deleteComment(comment.get_id());
                post.getCommentList().remove(comment);
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

    public Response updateComments(String postId, String commentId, String content, MultipartFile img) {
        Response response = new Response();

        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new OurException("Comment Not Found"));
            postRepository.findById(postId).orElseThrow(() -> new OurException("Post Not Found"));

            if (content == null || content.isEmpty() && img == null) {
                response.setStatusCode(400);
                response.setMessage("content field is empty");

                return response;
            }

            if (img != null) {
                String imageUrl = awsS3Config.saveImageToS3(img);
                comment.setImgUrl(imageUrl);
            }

            comment.setContent(content);

            Comment savedComment = commentRepository.save(comment);
            CommentDTO commentDTO = CommentMapper.mapEntityToDTO(savedComment);

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

    public Response createComments(String postId, String userId, String content, MultipartFile img) {
        Response response = new Response();

        try {
            Post post = postRepository.findById(postId).orElseThrow(() -> new OurException("Post Not Found"));
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            Comment comment = new Comment();
            comment.setUser(user);
            comment.setPost(post);

            if (content == null || content.isEmpty() && img == null) {
                response.setStatusCode(400);
                response.setMessage("content field is empty");

                return response;
            }

            if (img != null) {
                String imageUrl = awsS3Config.saveImageToS3(img);
                comment.setImgUrl(imageUrl);
            }

            comment.setContent(content);

            Comment savedComment = commentRepository.save(comment);
            CommentDTO commentDTO = CommentMapper.mapEntityToDTO(savedComment);

            post.getCommentList().add(savedComment);
            postRepository.save(post);

            Notification notification = new Notification("comment", user, post.getUser());
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
}
