package com.yukeon.wantedpreonboardingbackend.post.domain.repository;

import com.yukeon.wantedpreonboardingbackend.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
