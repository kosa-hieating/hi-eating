package kr.or.hieating.review.controller;

import kr.or.hieating.review.dto.ProductReviewPageResponseDto;
import kr.or.hieating.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductReviewRestController {

  private final ReviewService reviewService;

  @GetMapping("/api/products/{productId}/reviews")
  public ProductReviewPageResponseDto productReviews(
      @PathVariable Long productId,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "5") int size) {
    return reviewService.findProductReviews(productId, page, size);
  }
}
