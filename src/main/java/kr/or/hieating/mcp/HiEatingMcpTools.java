package kr.or.hieating.mcp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.or.hieating.category.dto.CategoryMenuResponseDto;
import kr.or.hieating.category.service.CategoryService;
import kr.or.hieating.hotdeal.dto.ActiveHotDealResponseDto;
import kr.or.hieating.hotdeal.dto.HotDealProductListPageResponseDto;
import kr.or.hieating.hotdeal.dto.HotDealProductSearchCondition;
import kr.or.hieating.hotdeal.dto.HotDealProductsResponseDto;
import kr.or.hieating.hotdeal.service.HotDealService;
import kr.or.hieating.product.domain.ProductDetail;
import kr.or.hieating.product.dto.MostPurchasedProductResponseDto;
import kr.or.hieating.product.dto.ProductListPageResponseDto;
import kr.or.hieating.product.dto.ProductListSearchCondition;
import kr.or.hieating.product.dto.ProductSearchCondition;
import kr.or.hieating.product.service.ProductSearchService;
import kr.or.hieating.product.service.ProductService;
import kr.or.hieating.promotion.domain.Promotions;
import kr.or.hieating.promotion.service.PromotionService;
import kr.or.hieating.review.domain.ReviewSummary;
import kr.or.hieating.review.dto.ProductReviewPageResponseDto;
import kr.or.hieating.review.dto.ProductReviewResponseDto;
import kr.or.hieating.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HiEatingMcpTools {

  private final CategoryService categoryService;
  private final ProductService productService;
  private final ProductSearchService productSearchService;
  private final HotDealService hotDealService;
  private final PromotionService promotionService;
  private final ReviewService reviewService;

  @McpTool(name = "list_categories", description = "List product categories in Hi Eating.")
  public List<CategoryMenuResponseDto> listCategories() {
    return categoryService.findCategories();
  }

  @McpTool(
      name = "search_products",
      description = "Search Hi Eating products by keyword, price, discount, and sort order.")
  public ProductListPageResponseDto searchProducts(
      @McpToolParam(description = "Keyword to search in product names.", required = true)
          String keyword,
      @McpToolParam(description = "Minimum product price filter.", required = false)
          Integer minPrice,
      @McpToolParam(description = "Maximum product price filter.", required = false)
          Integer maxPrice,
      @McpToolParam(description = "Minimum discount rate filter.", required = false)
          Integer minDiscountRate,
      @McpToolParam(
              description = "Sort order. Use popular, latest, priceAsc, or priceDesc.",
              required = false)
          String sort,
      @McpToolParam(description = "Page number starting from 1.", required = false) Integer page) {
    return productSearchService.searchProducts(
        new ProductSearchCondition(
            keyword, null, minPrice, maxPrice, minDiscountRate, sort, page));
  }

  @McpTool(
      name = "list_products_by_category",
      description = "List Hi Eating products in a category with optional filters.")
  public ProductListPageResponseDto listProductsByCategory(
      @McpToolParam(description = "Category ID.", required = true) Long categoryId,
      @McpToolParam(description = "Minimum product price filter.", required = false)
          Integer minPrice,
      @McpToolParam(description = "Maximum product price filter.", required = false)
          Integer maxPrice,
      @McpToolParam(description = "Minimum discount rate filter.", required = false)
          Integer minDiscountRate,
      @McpToolParam(
              description = "Sort order. Use popular, latest, priceAsc, or priceDesc.",
              required = false)
          String sort,
      @McpToolParam(description = "Page number starting from 1.", required = false) Integer page) {
    return productService.findProductsByCategory(
        new ProductListSearchCondition(
            categoryId, null, minPrice, maxPrice, minDiscountRate, sort, page));
  }

  @McpTool(name = "get_product_detail", description = "Get full detail for one Hi Eating product.")
  public ProductDetail getProductDetail(
      @McpToolParam(description = "Product ID.", required = true) Long productId) {
    return productService
        .findProductDetail(productId)
        .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
  }

  @McpTool(
      name = "list_most_purchased_products",
      description = "List most purchased products shown on the Hi Eating home page.")
  public List<MostPurchasedProductResponseDto> listMostPurchasedProducts() {
    return productService.findMostPurchasedProducts();
  }

  @McpTool(name = "list_active_hot_deals", description = "List active Hi Eating hot deals.")
  public List<ActiveHotDealResponseDto> listActiveHotDeals() {
    return hotDealService.findActiveHotDeals();
  }

  @McpTool(
      name = "list_active_hot_deal_products",
      description = "List products currently included in active Hi Eating hot deals.")
  public List<HotDealProductsResponseDto> listActiveHotDealProducts() {
    return hotDealService.findActiveHotDealProducts();
  }

  @McpTool(
      name = "list_hot_deal_products",
      description = "List products for a specific hot deal with pagination and sorting.")
  public HotDealProductListPageResponseDto listHotDealProducts(
      @McpToolParam(description = "Hot deal ID.", required = true) Long hotDealId,
      @McpToolParam(
              description = "Sort order. Use popular, latest, priceAsc, or priceDesc.",
              required = false)
          String sort,
      @McpToolParam(description = "Page number starting from 1.", required = false) Integer page,
      @McpToolParam(description = "Page size from 1 to 40.", required = false) Integer size) {
    return hotDealService.findHotDealProducts(
        new HotDealProductSearchCondition(hotDealId, null, sort, page, size));
  }

  @McpTool(name = "list_active_promotions", description = "List active Hi Eating promotions.")
  public List<Promotions> listActivePromotions() {
    return promotionService.findActivePromotions();
  }

  @McpTool(
      name = "list_product_reviews",
      description = "List reviews for a specific Hi Eating product.")
  public ProductReviewPageResponseDto listProductReviews(
      @McpToolParam(description = "Product ID.", required = true) Long productId,
      @McpToolParam(description = "Page number starting from 1.", required = false) Integer page,
      @McpToolParam(description = "Page size from 1 to 50.", required = false) Integer size) {
    return reviewService.findProductReviews(
        productId, normalizePage(page), normalizeReviewSize(size));
  }

  @McpTool(
      name = "get_product_review_summary",
      description = "Get review summary and recent review excerpts for a Hi Eating product.")
  public ProductReviewSummaryResponse getProductReviewSummary(
      @McpToolParam(description = "Product ID.", required = true) Long productId,
      @McpToolParam(description = "Number of recent review excerpts from 1 to 20.", required = false)
          Integer recentReviewCount) {
    ProductDetail product = findProductOrThrow(productId);
    ReviewSummary summary = product.reviewSummary();
    int size = normalizeRecentReviewCount(recentReviewCount);
    List<ReviewExcerpt> recentReviews =
        reviewService.findProductReviews(productId, 1, size).items().stream()
            .map(HiEatingMcpTools::toReviewExcerpt)
            .toList();

    return new ProductReviewSummaryResponse(
        product.id(),
        product.name(),
        summary.averageRating(),
        summary.reviewCount(),
        recentReviews);
  }

  @McpTool(
      name = "analyze_product_reviews",
      description =
          "Analyze product reviews by rating-based sentiment and return representative review excerpts.")
  public ProductReviewAnalysisResponse analyzeProductReviews(
      @McpToolParam(description = "Product ID.", required = true) Long productId,
      @McpToolParam(description = "Maximum reviews to analyze from 1 to 200.", required = false)
          Integer maxReviewCount) {
    ProductDetail product = findProductOrThrow(productId);
    List<ProductReviewResponseDto> reviews = loadReviewsForAnalysis(productId, maxReviewCount);

    int positiveCount = 0;
    int neutralCount = 0;
    int negativeCount = 0;
    int ratingTotal = 0;
    List<ReviewExcerpt> positiveExamples = new ArrayList<>();
    List<ReviewExcerpt> negativeExamples = new ArrayList<>();

    for (ProductReviewResponseDto review : reviews) {
      ratingTotal += review.getRating();
      if (review.getRating() >= 4) {
        positiveCount++;
        addExample(positiveExamples, review);
      } else if (review.getRating() <= 2) {
        negativeCount++;
        addExample(negativeExamples, review);
      } else {
        neutralCount++;
      }
    }

    double averageRating = reviews.isEmpty() ? 0.0 : (double) ratingTotal / reviews.size();

    return new ProductReviewAnalysisResponse(
        product.id(),
        product.name(),
        reviews.size(),
        averageRating,
        positiveCount,
        neutralCount,
        negativeCount,
        positiveExamples,
        negativeExamples);
  }

  private ProductDetail findProductOrThrow(Long productId) {
    return productService
        .findProductDetail(productId)
        .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
  }

  private List<ProductReviewResponseDto> loadReviewsForAnalysis(
      Long productId, Integer maxReviewCount) {
    int targetCount = normalizeAnalysisReviewCount(maxReviewCount);
    List<ProductReviewResponseDto> reviews = new ArrayList<>();
    int page = 1;
    int size = 50;

    while (reviews.size() < targetCount) {
      ProductReviewPageResponseDto reviewPage =
          reviewService.findProductReviews(productId, page, size);
      if (reviewPage.items().isEmpty()) {
        break;
      }

      int remaining = targetCount - reviews.size();
      reviews.addAll(reviewPage.items().stream().limit(remaining).toList());

      if (page >= reviewPage.totalPages()) {
        break;
      }
      page++;
    }

    return reviews;
  }

  private static void addExample(List<ReviewExcerpt> examples, ProductReviewResponseDto review) {
    if (examples.size() < 5) {
      examples.add(toReviewExcerpt(review));
    }
  }

  private static ReviewExcerpt toReviewExcerpt(ProductReviewResponseDto review) {
    return new ReviewExcerpt(
        review.getId(),
        review.getRating(),
        review.getReviewerName(),
        truncate(review.getContent()),
        review.getCreatedAt());
  }

  private static String truncate(String value) {
    if (value == null || value.length() <= 500) {
      return value;
    }
    return value.substring(0, 500);
  }

  private static int normalizePage(Integer page) {
    return Math.max(page == null ? 1 : page, 1);
  }

  private static int normalizeReviewSize(Integer size) {
    return Math.min(Math.max(size == null ? 10 : size, 1), 50);
  }

  private static int normalizeRecentReviewCount(Integer recentReviewCount) {
    return Math.min(Math.max(recentReviewCount == null ? 5 : recentReviewCount, 1), 20);
  }

  private static int normalizeAnalysisReviewCount(Integer maxReviewCount) {
    return Math.min(Math.max(maxReviewCount == null ? 100 : maxReviewCount, 1), 200);
  }

  public record ProductReviewSummaryResponse(
      Long productId,
      String productName,
      double averageRating,
      int reviewCount,
      List<ReviewExcerpt> recentReviews) {}

  public record ProductReviewAnalysisResponse(
      Long productId,
      String productName,
      int analyzedReviewCount,
      double averageRating,
      int positiveCount,
      int neutralCount,
      int negativeCount,
      List<ReviewExcerpt> positiveExamples,
      List<ReviewExcerpt> negativeExamples) {}

  public record ReviewExcerpt(
      Long reviewId, int rating, String reviewerName, String content, LocalDateTime createdAt) {}
}
