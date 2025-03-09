package com.yunha.flexforumback.forum.repository;

import com.yunha.flexforumback.forum.dto.ForumDTO;
import com.yunha.flexforumback.forum.entity.Forum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ForumRepository extends JpaRepository<Forum, Long> {

    @Query("SELECT f FROM Forum f")
    Page<Forum> findAllForumPage(Pageable pageable);

    @Query("SELECT new com.yunha.flexforumback.forum.dto.ForumDTO(f.forumCode, f.title, f.content, f.user.id, f.createAt, f.views) FROM Forum f")
    Page<ForumDTO> findAllForumDTOPage(Pageable pageable);

    @Query("SELECT new com.yunha.flexforumback.forum.dto.ForumDTO(f.title, f.content, f.user.id, f.createAt, f.views) FROM Forum f WHERE f.forumCode = :forumCode")
    ForumDTO findByForumCode(Long forumCode);

    @Modifying
    @Query("UPDATE Forum f SET f.views = f.views + 1 WHERE f.forumCode = :forumCode")
    void incrementViewCount(Long forumCode);

    @Query("SELECT COUNT(f) FROM Forum f WHERE f.user.userCode=:userCode")
    int getUserWrittenPostCount(Long userCode);
}
