package com.yunha.flexforumback.forum.service;

import com.yunha.flexforumback.forum.dto.CommentDTO;
import com.yunha.flexforumback.forum.dto.UserDTO;
import com.yunha.flexforumback.forum.entity.Comment;
import com.yunha.flexforumback.forum.repository.CommentRepository;
import com.yunha.flexforumback.forum.repository.ForumRepository;
import com.yunha.flexforumback.security.dto.CustomUserDetails;
import com.yunha.flexforumback.security.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {


    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ForumRepository forumRepository;

    public CommentService(UserRepository userRepository, CommentRepository commentRepository, ForumRepository forumRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.forumRepository = forumRepository;
    }


    /* 댓글 등록 */
    public void registComment(CustomUserDetails user, CommentDTO commentDTO, Long forumCode, String clientIp) {

        Comment newComment = new Comment(
                userRepository.findById(user.getUsername()),
                forumCode,
                commentDTO.getContent(),
                LocalDateTime.now(),
                clientIp
        );
        commentRepository.save(newComment);

    }

    public String removeComment(Long commentCode) {

        commentRepository.deleteById(commentCode);
        return "댓글 삭제 완료";
    }


    public Page<CommentDTO> getCommentList(Pageable pageable, Long forumCode) {

        Page<Comment> commentEntities = commentRepository.findAllByForumCode(pageable, forumCode);
        List<CommentDTO> comments = commentEntities.getContent().stream().map(c -> new CommentDTO(
                c.getCommentCode(),
                new UserDTO(c.getUser().getUserCode(),c.getUser().getId(),c.getUser().getUserRole()), // c.getUser하면 안되나?
                c.getForumCode(),
                c.getContent(),
                c.getCreateAt(),
                c.getIpAddress(),
                c.getRecommends().size()
        )).collect(Collectors.toList());
        return new PageImpl<>(comments,commentEntities.getPageable(),commentEntities.getTotalElements());
    }
}
