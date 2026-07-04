package kr.or.hieating.auth.service;

import java.util.List;
import java.util.stream.Collectors;
import kr.or.hieating.auth.domain.Users;
import kr.or.hieating.auth.mapper.AuthMapper;
import kr.or.hieating.auth.security.HiEatingUserPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class HiEatingUserDetailsService implements UserDetailsService {
  private final AuthMapper authMapper; // MyBatis 매퍼 공통 주입

  public HiEatingUserDetailsService(AuthMapper authMapper) {
    this.authMapper = authMapper;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    // 로그인 버튼 누르면 자동 호출되는 메서드
    Users myUser = authMapper.findByEmail(email);
    if (myUser == null) {
      throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
    }

    // 권한 가져오기
    List<String> authStrings = authMapper.findAuthoritiesByUserId(myUser.getId());

    List<SimpleGrantedAuthority> authorities =
        authStrings.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

    // Principal에서 DB 사용자 id를 바로 꺼낼 수 있도록 커스텀 UserDetails를 반환한다.
    return new HiEatingUserPrincipal(
        myUser.getId(), myUser.getEmail(), myUser.getPassword(), myUser.getName(), authorities);
  }
}
