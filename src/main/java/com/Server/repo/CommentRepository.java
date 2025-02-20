package com.Server.repo;

import com.Server.entity.Comment;
import com.Server.entity.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPost(Post post);
}
