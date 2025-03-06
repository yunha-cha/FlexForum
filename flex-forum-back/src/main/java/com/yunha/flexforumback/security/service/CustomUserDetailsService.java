package com.yunha.flexforumback.security.service;

import com.yunha.flexforumback.security.dto.CustomUserDetails;
import com.yunha.flexforumback.security.entity.User;
import com.yunha.flexforumback.security.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //매니저가 UserDetailsService 를 구현한 클래스를 찾아 이 메서드를 실행시킨다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userData = userRepository.findById(username);   //유저의 정보를 DB 에서 조회해오고,
        if(userData != null){
            return new CustomUserDetails(userData); //유저가 있다면 내가 만든 CustomUserDetails 의 형태로 return 한다. CustomUserDetails 보러가셈
        }

        return null;
    }
}
