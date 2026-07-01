package kr.or.hieating.product.admin.dto;

import java.util.List;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageResponseDTO {
  private List<ProductSearchResponseDTO> list;
  private int totalCount;
  private int totalPages;
  private int currentPage;
}
