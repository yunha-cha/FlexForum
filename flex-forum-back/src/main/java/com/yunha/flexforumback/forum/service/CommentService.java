package com.yunha.flexforumback.forum.service;

import com.yunha.flexforumback.forum.dto.CommentDTO;
import com.yunha.flexforumback.forum.entity.Comment;
import com.yunha.flexforumback.forum.entity.CommentRecommend;
import com.yunha.flexforumback.forum.repository.CommentRecommendRepository;
import com.yunha.flexforumback.forum.repository.CommentRepository;
import com.yunha.flexforumback.forum.repository.ForumRepository;
import com.yunha.flexforumback.security.dto.CustomUserDetails;
import com.yunha.flexforumback.security.entity.User;
import com.yunha.flexforumback.security.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {


    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ForumRepository forumRepository;

    private final CommentRecommendRepository commentRecommendRepository;

    public CommentService(UserRepository userRepository, CommentRepository commentRepository, ForumRepository forumRepository, CommentRecommendRepository commentRecommendRepository) {
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.forumRepository = forumRepository;
        this.commentRecommendRepository = commentRecommendRepository;
    }


    /* 댓글 등록 */
    @Transactional
    public void registComment(CustomUserDetails user, CommentDTO commentDTO, Long forumCode, String clientIp) {

        Comment newComment = new Comment(
                userRepository.findById(user.getUsername()),
                forumRepository.getReferenceById(forumCode),
                commentDTO.getContent(),
                LocalDateTime.now(),
                clientIp
        );
        commentRepository.save(newComment);

    }


    @Transactional
    public String removeComment(Long commentCode) {

        commentRepository.deleteById(commentCode);
        return "댓글 삭제 완료";
    }



    @Transactional
    public Page<CommentDTO> getCommentList(Pageable pageable, Long forumCode) {


        // jpql
        // select c from CommentRecommend c where c.comment.user = :user AND c.forum.forumCode = :forumCode
//        Boolean isRecommend = commentRecommendRepository.existsByUserAndForumCode();

        Page<Comment> commentEntities = commentRepository.findAllByForumCode(pageable, forumCode);
        List<CommentDTO> comments = commentEntities.getContent().stream().map(c -> new CommentDTO(
                c.getCommentCode(),
                c.getUser().getId(), // user엔티티의 모든 필드가 노출되면 안되니까 id만.(userId)
                c.getForum().getForumCode(),
                c.getContent(),
                c.getCreateAt(),
                c.getIpAddress(),
                c.getRecommends().size()
                // 댓글 추천 했는지 안했는지 여부

        )).collect(Collectors.toList());
        return new PageImpl<>(comments,commentEntities.getPageable(),commentEntities.getTotalElements());
    }



    @Transactional
    public String recommendComment(Long userCode, Long commentCode) {


        User user = userRepository.getReferenceById(userCode);
        Comment comment = commentRepository.getReferenceById(commentCode);

        // 존재 여부 확인 시 추가, 삭제
        if(commentRecommendRepository.existsByUserAndComment(user, comment)){
            commentRecommendRepository.deleteByUserAndComment(user, comment);
            System.out.println("구경금지 = ");


        } else {
            commentRecommendRepository.save(new CommentRecommend(user, comment, LocalDateTime.now()));

        }

        return "댓글추천";

    }
}
