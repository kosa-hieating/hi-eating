package kr.or.hieating.product.admin.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductSearchResponseDTO {
  private Long productId;
  private Long productOptionId;
  private String name;
  private String categoryName;
  private Integer price;
  private Integer stock;
  private LocalDate expireDate;

  /** 유통기한과 오늘 날짜를 비교하여 유통 상태를 실시간으로 판별 */
  public String getStatus() {
    if (this.expireDate == null) {
      return "일반";
    }

    long daysLeft = ChronoUnit.DAYS.between(LocalDate.now(), this.expireDate);

    return (daysLeft >= 0 && daysLeft <= 7) ? "유통임박" : "일반";
  }
}
