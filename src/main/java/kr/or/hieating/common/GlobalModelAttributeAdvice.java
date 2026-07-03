package kr.or.hieating.common;

import java.util.List;
import kr.or.hieating.auth.dto.AdminUserDto;
import kr.or.hieating.auth.security.HiEatingUserPrincipal;
import kr.or.hieating.category.dto.CategoryMenuResponseDto;
import kr.or.hieating.category.service.CategoryService;
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

  @ModelAttribute("headerCategories")
  public List<CategoryMenuResponseDto> headerCategories() {
    return categoryService.findCategories();
  }

  @ModelAttribute("currentUser")
  public AdminUserDto currentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null
        || !authentication.isAuthenticated()
        || authentication instanceof AnonymousAuthenticationToken) {
      return null;
    }

    Object principal = authentication.getPrincipal();
    if (!(principal instanceof HiEatingUserPrincipal userPrincipal)) {
      return null;
    }

    return AdminUserDto.builder()
        .id(userPrincipal.getId())
        .name(userPrincipal.getDisplayName())
        .email(userPrincipal.getEmail())
        .build();
  }
}
