package me.leeyunjin.login.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "nickname", unique = true) //ouath 사용자 이름
    private String nickname;

    @Builder(builderMethodName = "nicknameBuilder") //생성자에 닉네임 추가
    public User(String email, String password, String nickname){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public User update(String nickname){
        this.nickname = nickname;
        return this;
    }

    @Builder(builderMethodName = "authBuilder")
    public User(String email, String password){
        this.email = email;
        this.password = password;
        //this.auth = auth;
    }
    //사용자가 가지고있는 권한의 목록을 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("user")); //사용자이외의 권한이 없으므로 user 권한만 반환
    }
    //사용자의 id를 반환 (고유한 값)
    @Override
    public String getUsername(){
        return email;
    }

    //사용자의 패스워드 반환
    @Override
    public String getPassword() {
        return password;
    }

    //계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired(){
        return true; // 만료되지 않았음
    }
    //계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked(){
        return true; //잠금되지 않았음
    }
    //패스워드 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired(){
        return true; // 만료되지 않았음
    }
    //계정 사용 여부 반환
    @Override
    public boolean isEnabled(){
        return true;
    }
}
