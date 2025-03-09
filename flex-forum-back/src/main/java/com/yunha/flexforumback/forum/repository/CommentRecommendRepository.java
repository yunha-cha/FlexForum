package com.yunha.flexforumback.forum.repository;

import com.yunha.flexforumback.forum.entity.Comment;
import com.yunha.flexforumback.forum.entity.CommentRecommend;
import com.yunha.flexforumback.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface CommentRecommendRepository extends JpaRepository<CommentRecommend, Long> {


    Boolean existsByUserUserCodeAndCommentCommentCode(Long userCode, Long commentCode);

    @Transactional
    void deleteByUserUserCodeAndCommentCommentCode(Long userCode, Long commentCode);

    int countByCommentCommentCode(Long commentCode);
}
