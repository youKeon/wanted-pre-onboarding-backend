package com.yukeon.wantedpreonboardingbackend.post.dto.response;

import com.yukeon.wantedpreonboardingbackend.post.domain.Post;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PostsResponse {
    private long numberOfPosts;
    private boolean isLastPage;
    private List<PostResponse> postInfoResponses;

    public static PostsResponse of(Page<Post> posts) {
        return new PostsResponse(
                posts.getTotalElements(),
                posts.isLast(),
                posts.stream()
                        .map(post -> PostResponse.from(post))
                        .collect(Collectors.toList())
        );
    }

    public boolean getIsLastPage() {
        return isLastPage;
    }

}
