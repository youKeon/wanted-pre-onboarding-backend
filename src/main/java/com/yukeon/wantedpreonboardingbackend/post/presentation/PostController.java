package com.yukeon.wantedpreonboardingbackend.post.presentation;

import com.yukeon.wantedpreonboardingbackend.member.util.MemberInfo;
import com.yukeon.wantedpreonboardingbackend.post.application.PostService;
import com.yukeon.wantedpreonboardingbackend.post.dto.request.PostCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> create(@AuthenticationPrincipal MemberInfo memberInfo,
                                       @RequestBody @Valid PostCreateRequest request) {
        postService.create(memberInfo, request);
        return ResponseEntity.noContent().build();
    }
}
