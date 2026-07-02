package kr.or.hieating.promotion.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PromotionUpdateRequestDTO {
  private String title;
  private String link;

  @NotNull(message = "프로모션 시작일은 필수 입력값입니다.")
  @JsonFormat(pattern = "yyyy.MM.dd")
  private LocalDate startsAt;

  @NotNull(message = "프로모션 종료일은 필수 입력값입니다.")
  @JsonFormat(pattern = "yyyy.MM.dd")
  private LocalDate endsAt;
}
