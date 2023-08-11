package com.yukeon.wantedpreonboardingbackend.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class PostUpdateRequest {
    @NotBlank(message = "공백일 수 없습니다.")
    private String title;
    @NotBlank(message = "공백일 수 없습니다.")
    private String content;
}
