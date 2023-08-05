package com.yukeon.wantedpreonboardingbackend.common.annotation;

import com.yukeon.wantedpreonboardingbackend.post.persistence.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public abstract class ServiceTest {

    @Autowired protected PostRepository postRepository;

}
