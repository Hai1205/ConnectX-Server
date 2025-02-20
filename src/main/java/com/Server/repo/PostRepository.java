package com.Server.repo;

import com.Server.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.Server.entity.Post;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByUserInOrderByCreatedAtDesc(List<User> followingPosts);
}
