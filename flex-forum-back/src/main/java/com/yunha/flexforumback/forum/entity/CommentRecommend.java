package com.yunha.flexforumback.forum.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_code")      // ?
    @JsonBackReference
    private Comment comment;
}
