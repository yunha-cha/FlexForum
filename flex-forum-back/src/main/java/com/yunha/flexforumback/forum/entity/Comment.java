package com.yunha.flexforumback.forum.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yunha.flexforumback.security.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "comment")
public class Comment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_code")
    private Long commentCode;

    @JoinColumn(name = "user_code")
    @ManyToOne
    private User user;

    @JoinColumn(name = "forum_code")
    @ManyToOne
    @JsonBackReference
    private Forum forum;

    @Column(name = "content")
    private String content;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "ip_address")
    private String ipAddress;

    @OneToMany(mappedBy = "comment", fetch = FetchType.LAZY)
    private List<CommentRecommend> recommends = new ArrayList<>();

    public Comment(User user, Forum forum, String content, LocalDateTime createAt, String ipAddress) {
        this.user = user;
        this.forum = forum;
        this.content = content;
        this.createAt = createAt;
        this.ipAddress = ipAddress;
    }
}
