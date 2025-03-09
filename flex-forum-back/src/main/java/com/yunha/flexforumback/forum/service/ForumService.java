package com.yunha.flexforumback.forum.service;

import com.yunha.flexforumback.common.Tool;
import com.yunha.flexforumback.forum.dto.AttachmentDTO;
import com.yunha.flexforumback.forum.dto.ForumDTO;
import com.yunha.flexforumback.forum.entity.Attachment;
import com.yunha.flexforumback.forum.entity.Forum;
import com.yunha.flexforumback.forum.entity.ForumRecommend;
import com.yunha.flexforumback.forum.repository.AttachmentRepository;
import com.yunha.flexforumback.forum.repository.CommentRepository;
import com.yunha.flexforumback.forum.repository.ForumRecommendRepository;
import com.yunha.flexforumback.forum.repository.ForumRepository;
import com.yunha.flexforumback.security.dto.CustomUserDetails;
import com.yunha.flexforumback.security.entity.User;
import com.yunha.flexforumback.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ForumService {

    @Value("${file.download-url}")
    private String downloadUrl;
    private final Tool tool;
    private final ForumRepository forumRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;
    private final CommentRepository commentRepository;

    private final ForumRecommendRepository forumRecommendRepository;

    public ForumService(Tool tool, ForumRepository forumRepository, UserRepository userRepository, AttachmentRepository attachmentRepository, CommentRepository commentRepository, ForumRecommendRepository forumRecommendRepository) {
        this.tool = tool;
        this.forumRepository = forumRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
        this.commentRepository = commentRepository;
        this.forumRecommendRepository = forumRecommendRepository;
    }


    public Page<ForumDTO> getForumList(Pageable pageable) {

//        Page<Forum> forumPage = forumRepository.findAllForumPage(pageable);
        Page<ForumDTO> forumPage = forumRepository.findAllForumDTOPage(pageable);

        // forumDTO.setForumRecommendCounts
        // forumDTO.setCommentCounts
        // Page 객체 당 ForumDTO 10개씩 페이징
        // 다음방법은 N+1 문제 발생(각 N개 추가 쿼리 발생 2N)
        for (ForumDTO forumDTO : forumPage.getContent()) {
            Long forumCode = forumDTO.getForumCode();
            forumDTO.setRecommendCounts(forumRecommendRepository.countByForumForumCode(forumCode));     // 5
            forumDTO.setCommentCounts(commentRepository.countByForumForumCode(forumCode));      // 5
        }

        return forumPage;
    }


    public ForumDTO getForumDetail(CustomUserDetails user, Long forumCode) {

//        User registUser = userRepository.findById(user.getUsername());
        User registUser = userRepository.getReferenceById(user.getUserCode());      // user 엔티티의 code만 필요한 경우 프록시 객체로 접근 가능

        ForumDTO forumDTO = forumRepository.findByForumCode(forumCode);
        List<AttachmentDTO> attachmentDTO = attachmentRepository.findAllByForumCode(forumCode); // 첨부파일 조회
        forumDTO.setFile(attachmentDTO);

        // 특정 게시글의 게시글 좋아요 개수 //


        // 현재 사용자의 게시글좋아요 상태 조회 //
        Boolean isRecommend = forumRecommendRepository.existsByUserUserCodeAndForumForumCode(registUser.getUserCode(), forumCode);
        forumDTO.setIsRecommend(isRecommend);

        return forumDTO;
    }


    @Transactional
    public String registForum(CustomUserDetails user, ForumDTO forumDTO, String remoteAddr) {

        User registUser = userRepository.findById(user.getUsername());
        Forum newForum = new Forum(
                forumDTO.getForumCode(),
                forumDTO.getTitle(),
                forumDTO.getContent(),
                registUser,
                LocalDateTime.now(),
                0,
                "비공개",
                remoteAddr
        );


        newForum = forumRepository.save(newForum);
//        forumRepository.save(newForum);

        List<MultipartFile> files = forumDTO.getFiles();
        if(files != null){
            List<Attachment> attachments = new ArrayList<>();
            for (MultipartFile f : files){
                String changedName = tool.upload(f);
                Attachment attachment = new Attachment(
                        newForum.getForumCode(),
                        changedName,
                        f.getOriginalFilename(),
                        downloadUrl+changedName,
                        0,
                        f.getSize(),
                        LocalDate.now()
                );
                attachments.add(attachment);
            }
            attachmentRepository.saveAll(attachments);
        }
        return "게시글이 등록되었습니다! 🎈";
    }


    @Transactional
    public String removeForum(Long forumCode) {
        Forum forum = forumRepository.findById(forumCode).orElseThrow();

        String regex = "src\\s*=\\s*\"[^\"]*?/download/([^\"]+?)\"";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(forum.getContent());
        ArrayList<String> fileNames = new ArrayList<>();

        while (matcher.find()) {
            fileNames.add(matcher.group(1));
        }
        String[] result = fileNames.toArray(new String[0]);

        // 결과 출력
        for (String fileName : result) {
            System.out.println(fileName);
            if(tool.deleteFile(fileName)){
                System.out.println("사진 삭제 성공");
            } else {
                System.out.println("사진 삭제 실패");
            }
        }
        forumRepository.deleteById(forumCode);
        return "게시글 삭제 완료";
    }


    @Transactional
    public void countView(Long forumCode) {
        forumRepository.incrementViewCount(forumCode);
    }

    @Transactional
    public String uploadEditorImg(MultipartFile file) {
        return tool.upload(file);
    }

    @Transactional
    public List<String> uploadAttachment(List<MultipartFile> files) {
        List<String> filePaths = new ArrayList<>();
        for(MultipartFile file : files){
            filePaths.add(tool.upload(file));
        }
        return filePaths;
    }


    @Transactional
    public String registForumRecommend(CustomUserDetails user, Long forumCode) {
        User registUser = userRepository.getReferenceById(user.getUserCode());
//        Forum forum = forumRepository.findById(forumCode).orElseThrow();
//
//        if(forumRecommendRepository.existsByUserAndForum(registUser, forum)){
//            forumRecommendRepository.deleteByUserAndForum(registUser, forum);
//        } else {
//            ForumRecommend forumRecommend = new ForumRecommend(registUser, forum, LocalDateTime.now());
//            forumRecommendRepository.save(forumRecommend);
//        }

        // 개선된 코드
        Boolean isRecommend = forumRecommendRepository.existsByUserUserCodeAndForumForumCode(registUser.getUserCode(), forumCode);
        if (isRecommend){
            forumRecommendRepository.deleteByUserUserCodeAndForumForumCode(registUser.getUserCode(), forumCode);
            System.out.println("게시글 좋아요 삭제");
        } else {
            forumRecommendRepository.save(new ForumRecommend(registUser, forumRepository.getReferenceById(forumCode), LocalDateTime.now()));
            System.out.println("게시글 좋아요 성공");

        }
        return "추천 등록 성공";
    }


}
