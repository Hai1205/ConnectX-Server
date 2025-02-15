package com.Server.repo;

import com.Server.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.Server.entity.Post;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    Page<Post> findByUserId(Pageable pageable, String userId);

    List<Post> findByUserId(String userId);

    List<Post> findByUserIdIn(List<String> following);
}
