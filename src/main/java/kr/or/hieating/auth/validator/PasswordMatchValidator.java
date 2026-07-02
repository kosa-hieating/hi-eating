package kr.or.hieating.auth.validator;

import java.util.Objects;
import kr.or.hieating.auth.dto.SignupRequestDto;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class PasswordMatchValidator implements SignupValidator {

  @Override
  public void validate(SignupRequestDto request) {
    if (!Objects.equals(request.getPassword(), request.getPasswordConfirm())) {
      throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }
  }
}
