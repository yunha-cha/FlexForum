package com.yunha.flexforumback.forum.service;

import com.yunha.flexforumback.common.Tool;
import com.yunha.flexforumback.forum.dto.AttachmentDTO;
import com.yunha.flexforumback.forum.dto.ForumDTO;
import com.yunha.flexforumback.forum.entity.Attachment;
import com.yunha.flexforumback.forum.entity.Forum;
import com.yunha.flexforumback.forum.entity.ForumRecommend;
import com.yunha.flexforumback.forum.repository.AttachmentRepository;
import com.yunha.flexforumback.forum.repository.ForumRecommendRepository;
import com.yunha.flexforumback.forum.repository.ForumRepository;
import com.yunha.flexforumback.security.dto.CustomUserDetails;
import com.yunha.flexforumback.security.entity.User;
import com.yunha.flexforumback.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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

@Service
public class ForumService {

    @Value("${file.download-url}")
    private String downloadUrl;
    private final Tool tool;
    private final ForumRepository forumRepository;
    private final UserRepository userRepository;
    private final AttachmentRepository attachmentRepository;

    private final ForumRecommendRepository forumRecommendRepository;

    public ForumService(Tool tool, ForumRepository forumRepository, UserRepository userRepository, AttachmentRepository attachmentRepository, ForumRecommendRepository forumRecommendRepository) {
        this.tool = tool;
        this.forumRepository = forumRepository;
        this.userRepository = userRepository;
        this.attachmentRepository = attachmentRepository;
        this.forumRecommendRepository = forumRecommendRepository;
    }


    public Page<ForumDTO> getForumList(Pageable pageable) {

        Page<ForumDTO> forumList = forumRepository.findAllForumPage(pageable);

        // repository에서 dto로 변환하여 가져올 경우,
        // 음 Forum으로 조회하여 count 할 수 없다.
        // forum으로 조회해서 가져온 count를 setRecommendCount해줘야 한다.



//        System.out.println(forumList.getContent());
        return forumList;
    }


    public ForumDTO getForumDetail(Long forumCode) {
        List<AttachmentDTO> attachmentDTO = attachmentRepository.findAllByForumCode(forumCode);
        ForumDTO forumDTO = forumRepository.findByForumCode(forumCode);

        forumRecommendRepository.existsBy


        forumDTO.setFile(attachmentDTO);
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


    public String uploadEditorImg(MultipartFile file) {
        return tool.upload(file);
    }

    public List<String> uploadAttachment(List<MultipartFile> files) {
        List<String> filePaths = new ArrayList<>();
        for(MultipartFile file : files){
            filePaths.add(tool.upload(file));
        }
        return filePaths;
    }

    public String registForumRecommend(CustomUserDetails user, Long forumCode) {
        User registUser = userRepository.findById(user.getUsername());
        ForumRecommend forumRecommend = new ForumRecommend(registUser, forumCode, LocalDateTime.now());
        System.out.println("forumRecommend = " + forumRecommend);
        forumRecommendRepository.save(forumRecommend);

        return "추천 등록 성공";
    }


    public String removeForumRecommend(String userName, Long forumCode) {
        User registUser = userRepository.findById(userName);

        forumRecommendRepository.deleteByUserAndForumCode(registUser, forumCode);
        return "삭제 성공";

    }
}
