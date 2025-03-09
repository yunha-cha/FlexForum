package com.yunha.flexforumback.forum.repository;

import com.yunha.flexforumback.forum.dto.CommentDTO;
import com.yunha.flexforumback.forum.entity.Comment;
import com.yunha.flexforumback.forum.entity.Forum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {


//    @Query("SELECT new com.yunha.flexforumback.forum.dto.CommentDTO(c.commentCode, c.user, c.content, c.createAt) FROM Comment c WHERE c.forumCode = :forumCode")
    @Query("SELECT c FROM Comment c WHERE c.forum.forumCode = :forumCode")
    Page<Comment> findAllByForumCode(Pageable pageable, Long forumCode);
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.user.userCode=:userCode")
    int getUserWrittenCommentCount(Long userCode);

    int countByForumForumCode(Long forumCode);
}
