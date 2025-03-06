package com.yunha.flexforumback.security.controller;

import com.yunha.flexforumback.security.dto.JoinDTO;
import com.yunha.flexforumback.security.service.JoinService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class JoinController {

    private final JoinService joinService;

    public JoinController(JoinService joinService) {
        this.joinService = joinService;
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinUser(JoinDTO joinDTO){
        try {
//            if(validateId(joinDTO.getId())&&validatePassword(joinDTO.getPassword())){
            if(true){
                joinService.joinUser(joinDTO);
                return ResponseEntity.ok().body("가입이 완료되었습니다! 🎉");
            } else {
                throw new Exception("아이디 비밀번호 형식이 다릅니다.");

            }
        } catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("회원가입에 실패했습니다. : "+e.getMessage());
        }
    }

    private boolean validateId(String id){
        boolean hasString = id.matches(".*[a-zA-Z].*");
        boolean hasNumber = id.matches(".*\\d.*");
        return hasString && hasNumber && id.length() >= 8;
    }
    private boolean validatePassword(String password) {
        boolean baseCheck = validateId(password);
        boolean hasSpecial = password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
        return baseCheck && hasSpecial;
    }
}
