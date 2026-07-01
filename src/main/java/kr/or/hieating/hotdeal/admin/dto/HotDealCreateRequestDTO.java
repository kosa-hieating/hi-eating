package kr.or.hieating.hotdeal.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HotDealCreateRequestDTO {
  @Size(max = 10, message = "핫딜 제목은 공백 포함 10글자 이하로 입력해주세요.")
  private String title;
  @Size(max = 200, message = "핫딜 설명은 공백 포함 200글자 이하로 입력해주세요.")
  private String description;

  @JsonFormat(pattern = "yyyy.MM.dd")
  private LocalDate startsAt;

  @JsonFormat(pattern = "yyyy.MM.dd")
  private LocalDate endsAt;

  private Integer discountRate;
  private List<ProductItemDTO> products;


  @Getter
  @Builder
  @NoArgsConstructor(access = AccessLevel.PROTECTED)
  @AllArgsConstructor
  public static class ProductItemDTO {
    private Integer productOptionId;
    private Integer originalPrice;
  }
}
