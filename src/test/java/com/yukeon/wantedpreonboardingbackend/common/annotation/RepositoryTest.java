package com.yukeon.wantedpreonboardingbackend.common.annotation;
import com.yukeon.wantedpreonboardingbackend.global.config.JpaConfig;
import com.yukeon.wantedpreonboardingbackend.member.persistence.MemberRepository;
import com.yukeon.wantedpreonboardingbackend.post.persistence.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(JpaConfig.class)
@ActiveProfiles("test")
public abstract class RepositoryTest {
    @Autowired
    protected PostRepository postRepository;
    @Autowired
    protected MemberRepository memberRepository;
}