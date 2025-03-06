package com.yunha.flexforumback.forum.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "attachment")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attachment_code")
    private Long attachmentCode;

    @JoinColumn(name = "forum_code")
//    @ManyToOne
    private Long forumCode;

    @Column(name = "changed_name")
    private String changedName;

    @Column(name = "original_name")
    private String originalName;

    @Column(name = "file_full_path")
    private String fileFullPath;

    @Column(name = "download_count")
    private int downloadCount;

    @Column(name = "size")
    private long size;

    @Column(name = "uploaded_date")
    private LocalDate uploadDate;

    public Attachment(Long forumCode, String changedName, String originalName, String fileFullPath, int downloadCount, long size, LocalDate uploadDate) {
        this.forumCode = forumCode;
        this.changedName = changedName;
        this.originalName = originalName;
        this.fileFullPath = fileFullPath;
        this.downloadCount = downloadCount;
        this.size = size;
        this.uploadDate = uploadDate;
    }
}
