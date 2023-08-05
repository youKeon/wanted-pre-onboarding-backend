package com.yukeon.wantedpreonboardingbackend.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class PostUpdateRequest {
    private String title;
    private String content;
}
