package me.leeyunjin.login.service;

import lombok.RequiredArgsConstructor;
import me.leeyunjin.login.domain.User;
import me.leeyunjin.login.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService { // 스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email){ //사용자 email로 사용자 정보를 가져오는 메서드
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(email));
    }
}
