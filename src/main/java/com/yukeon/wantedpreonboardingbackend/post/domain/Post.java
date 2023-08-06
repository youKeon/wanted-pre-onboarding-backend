package com.yukeon.wantedpreonboardingbackend.post.domain;

import com.yukeon.wantedpreonboardingbackend.common.BaseEntity;
import com.yukeon.wantedpreonboardingbackend.member.domain.Member;
import com.yukeon.wantedpreonboardingbackend.member.exception.InvalidMemberException;
import com.yukeon.wantedpreonboardingbackend.post.exception.InvalidPostException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private boolean isDeleted;

    public Post(Member member,
                String content,
                String title) {

        validateContent(content);
        validateTitle(title);

        this.member = member;
        this.content = content;
        this.title = title;
        this.isDeleted = false;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    private void validateContent(String content) {
        if (content.length() == 0) {
            throw new InvalidPostException("Content는 공백일 수 없습니다.");
        }
    }

    private void validateTitle(String title) {
        if (title.length() == 0) {
            throw new InvalidPostException("Title은 공백일 수 없습니다.");
        }
    }
}
