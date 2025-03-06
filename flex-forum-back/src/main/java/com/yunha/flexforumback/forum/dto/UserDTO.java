package com.yunha.flexforumback.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter@Setter
public class UserDTO {
    private Long userCode;
    private String userId;
    private String userRole;
}
