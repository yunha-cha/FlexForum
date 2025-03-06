package com.yunha.flexforumback.forum.controller;

import com.yunha.flexforumback.forum.dto.ForumDTO;
import com.yunha.flexforumback.forum.service.ForumService;
import com.yunha.flexforumback.security.dto.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("")
public class ForumController {

    private final ForumService forumService;

    public ForumController(ForumService forumService) {
        this.forumService = forumService;
    }


    /* 게시글 조회 페이징 */
    @GetMapping("/forum")
    public ResponseEntity<Page<ForumDTO>> getForumList(@RequestParam int page){

        Pageable pageable = PageRequest.of(page, 5, Sort.by("createAt").descending());

        Page<ForumDTO> forumList = forumService.getForumList(pageable);
        return ResponseEntity.ok().body(forumList);
    }



    /* 게시글 상세 조회 */
    @GetMapping("/forum/{forumCode}")
    public ResponseEntity<ForumDTO> getForumDetail(@PathVariable Long forumCode){

        return ResponseEntity.ok().body(forumService.getForumDetail(forumCode));

    }



    /* 게시글 등록 */
    @PostMapping("/forum")
    public ResponseEntity<String> registForum(@AuthenticationPrincipal CustomUserDetails user, ForumDTO forumDTO, HttpServletRequest request){

        return ResponseEntity.ok().body(forumService.registForum(user, forumDTO, request.getRemoteAddr()));
    }


    /* 게시글 삭제 */
    @DeleteMapping("/forum/{forumCode}")
    public ResponseEntity<String> removeForum(@PathVariable Long forumCode){

        return ResponseEntity.ok().body(forumService.removeForum(forumCode));
    }



    /* 게시글 추천 등록 */
    @PostMapping("/forum/{forumCode}/recommend")
    public ResponseEntity<String> registForumRecommend(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long forumCode){

        String result = forumService.registForumRecommend(user, forumCode);
        System.out.println("result = " + result);

        return ResponseEntity.ok().body(result);

    }

    @DeleteMapping("/forum/{forumCode}/recommend")
    public ResponseEntity<String> removeForumRecommend(@AuthenticationPrincipal CustomUserDetails user, @PathVariable Long forumCode){

        return ResponseEntity.ok().body(forumService.removeForumRecommend(user.getUsername(), forumCode));
    }



    /* 조회수 세기 */
    @PostMapping("/views/{forumCode}")
    public ResponseEntity<?> countView(@PathVariable Long forumCode){
        try{

            forumService.countView(forumCode);
           return ResponseEntity.ok().body("조회수 세기 성공");
        }catch (Exception e){
            e.printStackTrace(); // 예외 내용
            return ResponseEntity.badRequest().body("조회수 세기 실패");
        }
    }






    @PostMapping("/forum/image")
    public ResponseEntity<?> uploadEditorImg(@RequestParam MultipartFile file){

        String changedFileName = forumService.uploadEditorImg(file);

        return ResponseEntity.ok().body(changedFileName);

    }


    /* 첨부파일 업로드 */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadAttachment(@RequestParam List<MultipartFile> files){

        if (!files.isEmpty()) {
            return ResponseEntity.ok().body(forumService.uploadAttachment(files));
        }
        return ResponseEntity.badRequest().body("프론트로 가라");
    }




}
