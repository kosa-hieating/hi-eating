package kr.or.hieating.mypage.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageController {

  @GetMapping("/mypage")
  public String myPage(Model model) {
    model.addAttribute("memberName", "이재우");
    model.addAttribute(
        "summaryCards",
        List.of(
            new SummaryCard("주문 상품", 0),
            new SummaryCard("관심 상품", 0),
            new SummaryCard("최근 본 상품", 1)));
    model.addAttribute(
        "recentOrder",
        new RecentOrder(
            "구매 확정",
            "오트밀",
            "아침 그래놀라 오트밀 세트",
            "1팩 / HIEATING",
            "119,640원",
            "https://images.unsplash.com/photo-1517673132405-a56a62b18caf?auto=format&fit=crop&w=320&q=80"));
    model.addAttribute(
        "recommendedProducts",
        List.of(
            new RecommendedProduct(
                "하루를 채워주는 산뜻한 선택",
                "청정 목장 무항생제 우유 900ml",
                "3,900원",
                null,
                "4만원 이상 무료배송",
                "https://images.unsplash.com/photo-1563636619-e9143da7973b?auto=format&fit=crop&w=320&q=80"),
            new RecommendedProduct(
                "담백 깔끔한 맑은 국물",
                "[1Table] 한우 나주곰탕 400g",
                "5,760원",
                "20%",
                "4만원 이상 무료배송",
                "https://images.unsplash.com/photo-1547592180-85f173990554?auto=format&fit=crop&w=320&q=80"),
            new RecommendedProduct(
                "시간이 없다 싶을 때",
                "350분만에 6일 패키지 도시락",
                "6,000원",
                null,
                "4만원 이상 무료배송",
                "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&fit=crop&w=320&q=80"),
            new RecommendedProduct(
                "부드럽게 녹아드는 한우의 맛",
                "느리게 우려낸 한우 우거지탕 800g",
                "13,000원",
                null,
                "4만원 이상 무료배송",
                "https://images.unsplash.com/photo-1604908176997-125f25cc6f3d?auto=format&fit=crop&w=320&q=80"),
            new RecommendedProduct(
                "단백질 챙긴 가벼운 식사",
                "[비비드키친] 저당 닭가슴살 샐러드 3종 125g",
                "3,900원",
                null,
                "4만원 이상 무료배송",
                "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=320&q=80")));
    return "mypage/index";
  }

  public record SummaryCard(String label, int count) {}

  public record RecentOrder(
      String status, String brand, String name, String option, String price, String imageUrl) {}

  public record RecommendedProduct(
      String subtitle,
      String name,
      String price,
      String discountRate,
      String deliveryMessage,
      String imageUrl) {}
}
