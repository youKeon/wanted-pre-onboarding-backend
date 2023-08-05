package com.yukeon.wantedpreonboardingbackend.post.persistence;

import com.yukeon.wantedpreonboardingbackend.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = "SELECT p FROM Post p JOIN FETCH p.member WHERE p.id = :postId")
    Optional<Post> findPostWithMember(Long postId);
}
