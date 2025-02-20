package com.Server.service.api;

import com.Server.dto.*;
import com.Server.entity.*;
import com.Server.exception.OurException;
import com.Server.repo.*;
import com.Server.service.AwsS3Service;
import com.Server.utils.mapper.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Response createPost(List<MultipartFile> photos, String content, String userId) {
        Response response = new Response();

        try {
            Post formDataPost = new Post();

            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            formDataPost.setUser(user);

            if (photos != null && !photos.isEmpty()) {
                List<String> imageUrls = new ArrayList<>();
                for (MultipartFile photo : photos) {
                    String imageUrl = awsS3Service.saveImageToS3(photo);
                    imageUrls.add(imageUrl);
                }
                formDataPost.setImageUrlList(imageUrls);
            }

            formDataPost.setContent(content);

            Post post = postRepository.save(formDataPost);
            PostDTO postDTO = PostMapper.mapEntityToDTO(post);

            user.getPostList().add(post);
            userRepository.save(user);

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

            List<String> imageUrls = post.getImageUrlList();
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

    public Response likePost(String postId, String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new OurException("Post Not Found"));

            // Kiểm tra xem user đã like post chưa
            boolean isLiked = post.getLikeList()
                    .stream()
                    .map(dbRef -> dbRef.get_id().toString()) // Lấy ID từ DBRef
                    .anyMatch(id -> id.equals(userId)); // So sánh với userId

            if (isLiked) {
                // Nếu đã like, bỏ like
                post.getLikeList().removeIf(dbRef -> dbRef.get_id().toString().equals(userId));
                user.getLikedPostList().removeIf(dbRef -> dbRef.get_id().toString().equals(postId));

                response.setMessage("Post unliked successfully");
            } else {
                // Nếu chưa like, thêm like
                post.getLikeList().add(user);
                user.getLikedPostList().add(post);

                // Tạo thông báo
                Notification notification = new Notification("like", user, post.getUser());
                notificationRepository.save(notification);

                response.setMessage("Post liked successfully");
            }

            // Lưu dữ liệu
            userRepository.save(user);
            postRepository.save(post);

            response.setStatusCode(200);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error");
        }

        return response;
    }

    public Response getAllPosts() {
        Response response = new Response();

        try {
            List<Post> posts = postRepository.findAll();
            List<PostDTO> postDTOList = PostMapper.mapListEntityToListDTO(posts);

            response.setStatusCode(200);
            response.setMessage("successful");
            response.setPostDTOList(postDTOList);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage(e.getMessage());
            System.out.println(e.getMessage());
        }

        return response;
    }

    public Response getLikedPosts(String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            List<Post> posts = user.getLikedPostList();
            List<PostDTO> postDTOList = PostMapper.mapListEntityToListDTO(posts);

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

    public Response getFollowingPosts(String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            List<User> followingPosts = user.getFollowingList();

            if (followingPosts.isEmpty()) {
                response.setStatusCode(200);
                response.setMessage("User is not following anyone");
                response.setPostDTOList(new ArrayList<>());

                return response;
            }

            List<Post> feedPosts = postRepository.findByUserInOrderByCreatedAtDesc(followingPosts);
            List<PostDTO> postDTOList = PostMapper.mapListEntityToListDTO(feedPosts);

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

    public Response getUserPosts(String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            List<Post> userPosts = Optional.ofNullable(user.getPostList()).orElse(new ArrayList<>());
            List<Post> postShared = Optional.ofNullable(user.getSharedPostList()).orElse(new ArrayList<>());
            userPosts.addAll(postShared);

            List<PostDTO> postDTOList = PostMapper.mapListEntityToListDTO(userPosts);

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

    public Response getBookmarkedPosts(String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));

            List<Post> posts = user.getBookmarkedPostList();
            List<PostDTO> postDTOList = PostMapper.mapListEntityToListDTO(posts);

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

    public Response bookmarkPost(String postId, String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new OurException("Post Not Found"));

            boolean isBookmarked = post.getBookmarkList()
                    .stream()
                    .map(dbRef -> dbRef.get_id().toString())
                    .anyMatch(id -> id.equals(userId));

            if (isBookmarked) {
                post.getBookmarkList().removeIf(dbRef -> dbRef.get_id().toString().equals(userId));
                user.getBookmarkedPostList().removeIf(dbRef -> dbRef.get_id().toString().equals(postId));

                response.setMessage("Remove bookmark successfully");
            } else {
                post.getBookmarkList().add(user);
                user.getBookmarkedPostList().add(post);

                response.setMessage("Add bookmarked successfully");
            }

            userRepository.save(user);
            postRepository.save(post);

            response.setStatusCode(200);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error");
        }

        return response;
    }

    public Response sharePost(String postId, String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            Post post = postRepository.findById(postId).orElseThrow(() -> new OurException("Post Not Found"));

            boolean isShared = post.getBookmarkList()
                    .stream()
                    .map(dbRef -> dbRef.get_id().toString())
                    .anyMatch(id -> id.equals(userId));

            if (isShared) {
                post.getShareList().removeIf(dbRef -> dbRef.get_id().toString().equals(userId));
                user.getBookmarkedPostList().removeIf(dbRef -> dbRef.get_id().toString().equals(postId));

                response.setMessage("Post unshared successfully");
            } else {
                post.getShareList().add(user);
                user.getSharedPostList().add(post);

                response.setMessage("Post shared successfully");
            }

            userRepository.save(user);
            postRepository.save(post);

            response.setStatusCode(200);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Internal Server Error");
        }

        return response;
    }
}
