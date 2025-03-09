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
    public Page<CommentDTO> getCommentList(CustomUserDetails user, Pageable pageable, Long forumCode) {

        User currentUser = userRepository.getReferenceById(user.getUserCode());
        Page<CommentDTO> commentDTOPage = commentRepository.findAllByForumCode(pageable, forumCode);

        // 조회할 때 추천 개수, 자기 자신이 추천했는지 여부 추가
        for (CommentDTO c : commentDTOPage.getContent()) {      // 댓글 개수 *2 (2N)
            c.setRecommendCounts(commentRecommendRepository.countByCommentCommentCode(c.getCommentCode()));
            c.setIsRecommend(commentRecommendRepository.existsByUserUserCodeAndCommentCommentCode(currentUser.getUserCode(), c.getCommentCode()));
        }

        return commentDTOPage;

    }



    /* 댓글좋아요 */
    @Transactional
    public String recommendComment(Long userCode, Long commentCode) {

        User user = userRepository.getReferenceById(userCode);
        Comment comment = commentRepository.getReferenceById(commentCode);
        Boolean isRecommend = commentRecommendRepository.existsByUserUserCodeAndCommentCommentCode(user.getUserCode(), comment.getCommentCode());

        // 존재 여부 확인 시 추가, 삭제
        if(isRecommend){
            commentRecommendRepository.deleteByUserUserCodeAndCommentCommentCode(user.getUserCode(), comment.getCommentCode());
        } else {
            commentRecommendRepository.save(new CommentRecommend(user, comment, LocalDateTime.now()));
        }

        return "댓글추천";

    }
}
