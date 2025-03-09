package com.yunha.flexforumback.security.repository;

import com.yunha.flexforumback.security.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * 유저가 존재하는지 확인하는 메서드
     * @param accountId 확인할 ID
     * @return true or false
     */
    Boolean existsById(String accountId);

    User findById(String username);
    User findByUserCode(Long userCode);

    @Modifying
    @Query("UPDATE User u SET u.lastLoginTime = :now WHERE u.userCode = :userCode")
    void updateUserLoginTime(Long userCode, LocalDateTime now);
}
