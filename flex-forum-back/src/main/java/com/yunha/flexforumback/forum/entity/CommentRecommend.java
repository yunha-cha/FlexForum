package com.yunha.flexforumback.forum.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.yunha.flexforumback.security.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment_recommend")
@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class CommentRecommend {
    @Id
    @Column(name = "comment_recommend_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentRecommendCode;

    @JoinColumn(name = "user_code")
    @OneToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private User user;          // 댓글 작성자가 아닌 댓글 추천한 사람


    @JoinColumn(name = "comment_code")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    private Comment comment;


    @Column(name = "recommend_date")
    private LocalDateTime recommendDate;


    public CommentRecommend(User user, Comment comment, LocalDateTime recommendDate) {
        this.user = user;
        this.comment = comment;
        this.recommendDate = recommendDate;
    }
}
