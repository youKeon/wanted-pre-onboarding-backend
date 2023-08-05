package com.yukeon.wantedpreonboardingbackend.post.dto.response;

import com.yukeon.wantedpreonboardingbackend.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostResponse {
    private String title;
    private String content;

    public static PostResponse from(Post post) {
        return new PostResponse(post.getTitle(), post.getContent());
    }
}
