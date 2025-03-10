package com.yunha.flexforumback.forum.dto;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class CategoryDTO {

    private Long categoryCode;

    private String name;

    private boolean enable;


}
