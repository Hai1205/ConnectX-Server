package com.Server.repo;

import com.Server.entity.Post;
import com.Server.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    Optional<User> findByEmail(String  email);

    Optional<User> findByUsername(String  username);

    Page<User> findAll(Pageable pageable);
}
