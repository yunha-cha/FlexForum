package com.yunha.flexforumback.forum.repository;

import com.yunha.flexforumback.forum.entity.Comment;
import com.yunha.flexforumback.forum.entity.CommentRecommend;
import com.yunha.flexforumback.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRecommendRepository extends JpaRepository<CommentRecommend, Long> {

    boolean existsByUserAndComment(User user, Comment comment);

    void deleteByUserAndComment(User user, Comment comment);
}
