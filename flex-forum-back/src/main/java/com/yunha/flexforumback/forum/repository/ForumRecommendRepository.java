package com.yunha.flexforumback.forum.repository;

import com.yunha.flexforumback.forum.entity.Forum;
import com.yunha.flexforumback.forum.entity.ForumRecommend;
import com.yunha.flexforumback.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ForumRecommendRepository extends JpaRepository<ForumRecommend, Long> {

    //    Boolean existsByUserAndForum(User registUser, Forum f);
    Boolean existsByUserUserCodeAndForumForumCode(Long userCode, Long forumCode);

    //    void deleteByUserAndForum(User registUser, Forum forumCode);
    void deleteByUserUserCodeAndForumForumCode(Long userCode, Long forumCode);


    int countByForumForumCode(Long forumCode);
}
