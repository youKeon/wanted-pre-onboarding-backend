package com.yukeon.wantedpreonboardingbackend.post.dto.response;

import com.yukeon.wantedpreonboardingbackend.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostInfoResponse {
    private String title;
    private String content;

    public static PostInfoResponse from(Post post) {
        return new PostInfoResponse(post.getTitle(), post.getContent());
    }
}
