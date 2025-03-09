package com.yunha.flexforumback.forum.controller;


import com.yunha.flexforumback.forum.dto.CommentDTO;
import com.yunha.flexforumback.forum.service.CommentService;
import com.yunha.flexforumback.security.dto.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {


    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


    /* 댓글 조회 */
    // 10개씩 보여주는 걸로
    @GetMapping("/{forumCode}")
    public ResponseEntity<?> getCommentList(@AuthenticationPrincipal CustomUserDetails user ,@RequestParam int page ,@PathVariable Long forumCode){

        Pageable pageable = PageRequest.of(page, 10, Sort.by("createAt").descending());
        return ResponseEntity.ok().body(commentService.getCommentList(user, pageable, forumCode));
    }



    /* 댓글 등록 */
    @PostMapping("/{forumCode}")
    public ResponseEntity<String> registComment(@AuthenticationPrincipal CustomUserDetails user, CommentDTO commentDTO, @PathVariable Long forumCode, HttpServletRequest request){

        String clientIp = request.getRemoteAddr();
        try{
            commentService.registComment(user, commentDTO, forumCode, clientIp);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            return ResponseEntity.badRequest().build();
        }

    }


    /* 댓글 삭제 */
    @DeleteMapping("/{commentCode}")
    public ResponseEntity<?> removeComment(@PathVariable Long commentCode){

        return ResponseEntity.ok().body(commentService.removeComment(commentCode));
    }



    /* 댓글 좋아요 */
    @PostMapping("/{commentCode}/recommend")
    public ResponseEntity<String> recommendComment(@AuthenticationPrincipal CustomUserDetails user ,@PathVariable Long commentCode){

        return ResponseEntity.ok().body(commentService.recommendComment(user.getUserCode(), commentCode));
    }


}
