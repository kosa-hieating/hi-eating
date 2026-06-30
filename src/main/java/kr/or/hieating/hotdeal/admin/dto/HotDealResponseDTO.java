package kr.or.hieating.hotdeal.admin.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotDealResponseDTO {
    private Long id;
    private String title;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;
    private String status;
    private Integer productCount;
    private Integer discountPrice;
}
