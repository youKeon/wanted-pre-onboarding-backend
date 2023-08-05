package com.yukeon.wantedpreonboardingbackend.post.dto.request;

import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class PostCreateRequest {
    @NotBlank(message = "공백일 수 없습니다.")
    private String title;
    @NotBlank(message = "공백일 수 없습니다.")
    private String content;

    public Post toEntity(Member member) {
        return new Post(member, title, content);
    }
}