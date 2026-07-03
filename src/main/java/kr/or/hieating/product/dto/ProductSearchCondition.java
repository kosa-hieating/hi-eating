package kr.or.hieating.product.dto;

import lombok.Getter;

@Getter
public class ProductSearchCondition {

  public static final int DEFAULT_SIZE = 18;

  private final String keyword;
  private final String keywordLike;
  private final Long userId;
  private final Integer minPrice;
  private final Integer maxPrice;
  private final Integer minDiscountRate;
  private final String sort;
  private final int page;
  private final int size;
  private final int offset;

  public ProductSearchCondition(
      String keyword,
      Long userId,
      Integer minPrice,
      Integer maxPrice,
      Integer minDiscountRate,
      String sort,
      Integer page) {
    this.keyword = normalizeKeyword(keyword);
    this.keywordLike =
        this.keyword == null ? null : "%" + escapeLike(this.keyword.toLowerCase()) + "%";
    this.userId = userId;
    this.minPrice = normalizePositive(minPrice);
    this.maxPrice = normalizePositive(maxPrice);
    this.minDiscountRate = normalizePositive(minDiscountRate);
    this.sort = normalizeSort(sort);
    this.page = Math.max(page == null ? 1 : page, 1);
    this.size = DEFAULT_SIZE;
    this.offset = (this.page - 1) * this.size;
  }

  public boolean hasKeyword() {
    return keyword != null && !keyword.isBlank();
  }

  private static String normalizeKeyword(String keyword) {
    if (keyword == null || keyword.isBlank()) {
      return null;
    }
    return keyword.trim();
  }

  private static String escapeLike(String value) {
    return value.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
  }

  private static Integer normalizePositive(Integer value) {
    return value == null || value < 0 ? null : value;
  }

  private static String normalizeSort(String sort) {
    return switch (sort == null ? "popular" : sort) {
      case "latest", "priceAsc", "priceDesc" -> sort;
      default -> "popular";
    };
  }
}
