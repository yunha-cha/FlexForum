package com.yunha.flexforumback.forum.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class CommentRecommendDTO {
    private Long commentRecommendCode;
    private Long commentCode;
    private Long userCode;
}
