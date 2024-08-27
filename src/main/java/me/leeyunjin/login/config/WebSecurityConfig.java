package me.leeyunjin.login.config;

import lombok.RequiredArgsConstructor;
import me.leeyunjin.login.config.jwt.TokenProvider;
import me.leeyunjin.login.config.oauth.OAuth2UserCustomService;
import me.leeyunjin.login.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
//구글 로그인 관련 기능도 추가
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
    private final UserDetailService userDetailService;
    //이거 추가
    private final OAuth2UserCustomService oAuth2UserCustomService;

    //스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth // /login /signup /user로 요청이 오면 인증/인가 없이도 접근할 수 있음
                .requestMatchers(new AntPathRequestMatcher("/login"),
                        new AntPathRequestMatcher("/signup"),
                        new AntPathRequestMatcher("/user")
                ).permitAll()
                .anyRequest().authenticated()) //위에서 설정한 url이외 요청에 대해서 설정 + 별도에 인가는 필요하지않지만 인증이 성공된 상태여야 접근 가능
              .formLogin(formLogin -> formLogin
                .loginPage("/login")
                      .defaultSuccessUrl("/home", true)  // 로그인 이후에 바로 넘어가는 화면

              )
                .logout(logout -> logout //로그아웃 설정
                        .logoutSuccessUrl("/login") //로그아웃이 완료되었을때 이동할 경로
                        .invalidateHttpSession(true) // 로그아웃 이후에 세션을 전체 삭제할지 여부를 설정
                )
                .csrf(AbstractHttpConfigurer::disable) //csrf 비활성화 (활성화하는게 좋음)
                .build();
    }


    //인증 관리자 관련 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws  Exception{
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService); //사용자 정보 서비스 설정
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(authProvider);
    }
    // 비밀번호 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
