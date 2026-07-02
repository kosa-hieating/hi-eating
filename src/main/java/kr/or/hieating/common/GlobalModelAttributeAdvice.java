package kr.or.hieating.common;

import java.util.List;
import kr.or.hieating.category.dto.CategoryMenuResponseDto;
import kr.or.hieating.category.service.CategoryService;
import kr.or.hieating.auth.domain.Users;
import kr.or.hieating.auth.mapper.AuthMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalModelAttributeAdvice {

  private final CategoryService categoryService;
  private final AuthMapper authMapper;

  @ModelAttribute("headerCategories")
  public List<CategoryMenuResponseDto> headerCategories() {
    return categoryService.findCategories();
  }

  @ModelAttribute("currentUser")
  public Users currentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated() && 
        !(authentication instanceof AnonymousAuthenticationToken)) {
      String email = authentication.getName();
      return authMapper.findByEmail(email);
    }
    return null;
  }
}
