package com.yunha.flexforumback.forum.repository;

import com.yunha.flexforumback.forum.dto.AttachmentDTO;
import com.yunha.flexforumback.forum.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    @Query("SELECT new com.yunha.flexforumback.forum.dto.AttachmentDTO(a.attachmentCode, a.forumCode, a.changedName, a.originalName, a.fileFullPath, a.downloadCount, a.size, a.uploadDate) FROM Attachment a WHERE a.forumCode=:forumCode")
    List<AttachmentDTO> findAllByForumCode(Long forumCode);
}
