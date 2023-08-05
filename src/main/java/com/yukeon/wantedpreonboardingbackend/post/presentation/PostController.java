package com.yukeon.wantedpreonboardingbackend.post.presentation;

import com.yukeon.wantedpreonboardingbackend.member.util.MemberInfo;
import com.yukeon.wantedpreonboardingbackend.post.application.PostService;
import com.yukeon.wantedpreonboardingbackend.post.dto.request.PostCreateRequest;
import com.yukeon.wantedpreonboardingbackend.post.dto.request.PostUpdateRequest;
import com.yukeon.wantedpreonboardingbackend.post.dto.response.PostResponse;
import com.yukeon.wantedpreonboardingbackend.post.dto.response.PostsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    @PostMapping
    public void create(@AuthenticationPrincipal MemberInfo memberInfo,
                                       @RequestBody @Valid PostCreateRequest request) {
        postService.create(memberInfo, request);
    }

    @GetMapping
    public PostsResponse findAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        return postService.findAll(page - 1, size);
    }

    @GetMapping("/{id}")
    public PostResponse find(@PathVariable Long id) {
        return postService.find(id);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Long id,
                       @AuthenticationPrincipal MemberInfo memberInfo,
                       @RequestBody PostUpdateRequest request) {
        postService.update(id, memberInfo, request);
    }

    @DeleteMapping("/{id}")
    public void update(@PathVariable Long id,
                       @AuthenticationPrincipal MemberInfo memberInfo) {
        postService.delete(id, memberInfo);
    }
}
