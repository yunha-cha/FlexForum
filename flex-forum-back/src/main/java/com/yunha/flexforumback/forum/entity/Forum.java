package com.yunha.flexforumback.forum.entity;


import com.yunha.flexforumback.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Table(name = "forum")
public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "forum_code")
    private Long forumCode;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;


    @ManyToOne
    @JoinColumn(name = "user_code")
    private User user;      // Long userCode로 대공사

     // category vs categoryCode
    private Long categoryCode;

    @OneToMany(mappedBy = "forum", fetch = FetchType.LAZY)
    private List<Comment> commentList;

    @OneToMany(mappedBy = "forum", fetch = FetchType.LAZY)
    private List<ForumRecommend> forumRecommendList;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "views")
    private int views;

    @Column(name = "status")
    private String status;      // 공개, 비공개

    @Column(name = "ip_address")
    private String ipAddress;

    public Forum(Long forumCode, String title, String content, User user, LocalDateTime createAt, int views, String status, String ipAddress) {
        this.forumCode = forumCode;
        this.title = title;
        this.content = content;
        this.user = user;
        this.createAt = createAt;
        this.views = views;
        this.status = status;
        this.ipAddress = ipAddress;
    }
    // 등록

}
