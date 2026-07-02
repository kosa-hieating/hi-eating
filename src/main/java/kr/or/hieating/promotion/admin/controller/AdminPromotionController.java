package kr.or.hieating.promotion.admin.controller;

import java.util.List;
import kr.or.hieating.promotion.admin.service.AdminPromotionService;
import kr.or.hieating.promotion.domain.Promotions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/promotions")
@RequiredArgsConstructor
public class AdminPromotionController {

  private final AdminPromotionService adminPromotionService;

  @GetMapping
  public String getPromotionsPage(Model model) {
    List<Promotions> promotions = adminPromotionService.getAllPromotions();

    model.addAttribute("promotions", promotions);
    model.addAttribute("contentTemplate", "admin/promotions/settings");
    model.addAttribute("contentFragment", "content");
    model.addAttribute("pageStylesheet", "admin-promotion");
    model.addAttribute("pageScript", "admin-promotion");
    return "layout/admin-base";
  }
}
