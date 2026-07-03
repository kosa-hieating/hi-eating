package kr.or.hieating.review.controller;

import kr.or.hieating.review.dto.ReviewFormResponseDto;
import kr.or.hieating.review.service.ReviewService;
import kr.or.hieating.utils.UserResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;
  private final UserResolver userResolver;

  @GetMapping("/review/new")
  public String newReview(
      @RequestParam(required = false) Long purchaseId,
      @RequestParam(required = false) Long productId,
      Model model) {
    ReviewFormResponseDto reviewForm =
        reviewService.findReviewForm(userResolver.requireCurrentUserId(), purchaseId, productId);

    model.addAttribute("contentTemplate", "review/new");
    model.addAttribute("contentFragment", "content");
    model.addAttribute("pageStylesheet", "review");
    model.addAttribute("pageScript", "review");
    model.addAttribute("reviewForm", reviewForm);
    return "layout/base";
  }
}
