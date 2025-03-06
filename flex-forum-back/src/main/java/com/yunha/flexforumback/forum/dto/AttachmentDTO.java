package com.yunha.flexforumback.forum.dto;

import lombok.*;

import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AttachmentDTO {

    private Long attachmentCode;

    private Long forumCode;

    private String changedName;

    private String originalName;

    private String fileFullPath;

    private int downloadCount;

    private long size;

    private LocalDate uploadDate;
}
