package kr.or.hieating.auth.validator;

import java.util.Locale;
import kr.or.hieating.auth.dto.SignupRequestDto;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(8)
public class ReservedEmailValidator implements SignupValidator, EmailCheckValidator {

  private static final String DELETED_USER_EMAIL_DOMAIN = "@deleted.local";

  @Override
  public void validate(SignupRequestDto request) {
    validate(request.getEmail());
  }

  @Override
  public void validate(String email) {
    if (email == null) {
      return;
    }

    String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
    if (normalizedEmail.endsWith(DELETED_USER_EMAIL_DOMAIN)) {
      throw new IllegalArgumentException("사용할 수 없는 이메일입니다.");
    }
  }
}
