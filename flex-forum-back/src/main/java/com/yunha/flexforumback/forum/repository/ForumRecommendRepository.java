package com.yunha.flexforumback.forum.repository;

import com.yunha.flexforumback.forum.entity.ForumRecommend;
import com.yunha.flexforumback.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRecommendRepository extends JpaRepository<ForumRecommend, Long> {


    void deleteByUserAndForumCode(User registUser, Long forumCode);
}
