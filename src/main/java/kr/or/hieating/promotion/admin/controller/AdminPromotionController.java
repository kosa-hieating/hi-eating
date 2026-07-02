package kr.or.hieating.promotion.admin.controller;

import java.util.ArrayList;
import java.util.List;
import kr.or.hieating.promotion.domain.Promotions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/promotions")
public class AdminPromotionController {

  @GetMapping
  public String getPromotionsPage(Model model) {
    // 화면 파악을 위한 데이터
    List<Promotions> mockList = new ArrayList<>();
    
    Promotions p1 = new Promotions();
    p1.setId(1);
    p1.setTitle("프로모션IMG1");
    p1.setImgSrc("https://images.unsplash.com/photo-1615485290382-441e4d049cb5?auto=format&fit=crop&q=80&w=300");
    p1.setLink("https://localhost:8080/items/item1");
    p1.setDisplayOrder(1000);
    mockList.add(p1);


    model.addAttribute("promotions", mockList);
    model.addAttribute("contentTemplate", "admin/promotions/settings");
    model.addAttribute("contentFragment", "content");
    model.addAttribute("pageStylesheet", "admin-promotion");
    model.addAttribute("pageScript", "admin-promotion");
    return "layout/admin-base";
  }
}
