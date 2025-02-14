package com.Server.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Server.entity.Post;

public interface PostRepository extends MongoRepository<Post, String> {
}
