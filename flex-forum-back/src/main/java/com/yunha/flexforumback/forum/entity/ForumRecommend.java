package com.yunha.flexforumback.forum.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yunha.flexforumback.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "forum_recommend", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"forum_code", "user_code"})
})
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
@ToString
public class ForumRecommend {

    @Id
    @Column(name = "forum_recommend_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long forumRecommendCode;

    @JoinColumn(name = "user_code")
    @OneToOne(fetch = FetchType.LAZY)
    private User user;


    @JoinColumn(name = "forum_code")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Forum forum;

    @Column(name = "recommend_date")
    private LocalDateTime recommendDate;

    public ForumRecommend(User user, Forum forum, LocalDateTime recommendDate) {
        this.user = user;
        this.forum = forum;
        this.recommendDate = recommendDate;
    }
}
