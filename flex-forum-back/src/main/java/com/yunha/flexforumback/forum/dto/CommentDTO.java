package com.yunha.flexforumback.forum.dto;

import com.yunha.flexforumback.security.entity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CommentDTO {

    private Long commentCode;

    private String userId; // user -> userId

    private Long forumCode;

    private String content;

    private LocalDateTime createAt;

    private String ipAddress;

    private int recommendCount;

//    private Boolean isRecommend;

}
