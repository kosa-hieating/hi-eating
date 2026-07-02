package kr.or.hieating.promotion.admin.controller;

import java.util.List;
import kr.or.hieating.promotion.admin.service.AdminPromotionService;
import kr.or.hieating.promotion.domain.Promotions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/** 관리자용 프로모션 설정 화면을 관리하는 MVC 컨트롤러 클래스입니다. */
@Controller
@RequiredArgsConstructor
public class AdminPromotionController {

  // 관리자 프로모션 비즈니스 로직을 처리하는 서비스 객체 주입
  private final AdminPromotionService adminPromotionService;

  /**
   * 관리자 프로모션 설정 페이지를 조회하여 반환합니다.
   *
   * @param model 뷰에 데이터를 전달하기 위한 Model 객체
   * @return 관리자 레이아웃 템플릿 경로
   */
  @GetMapping("/admin/promotions")
  public String getPromotionsPage(Model model) {
    // DB에 등록된 모든 프로모션 목록 조회
    List<Promotions> promotions = adminPromotionService.getAllPromotions();

    // 조회한 프로모션 목록을 뷰 템플릿에 주입
    model.addAttribute("promotions", promotions);

    // 현재 날짜 및 시간을 주입하여 템플릿 내에서 배너 종료 여부(endsAt과 비교)를 판단할 수 있게 함
    model.addAttribute("now", java.time.LocalDateTime.now());

    // Thymeleaf 레이아웃 설정 (어떤 조각 템플릿과 CSS, JS를 적용할지 전달)
    model.addAttribute("contentTemplate", "admin/promotions/settings");
    model.addAttribute("contentFragment", "content");
    model.addAttribute("pageStylesheet", "admin-promotion");
    model.addAttribute("pageScript", "admin-promotion");

    // 공통 관리자 레이아웃 페이지로 렌더링을 위임
    return "layout/admin-base";
  }
}
