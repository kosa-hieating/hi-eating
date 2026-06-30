package kr.or.hieating.product.admin.dto;

import lombok.*;
import java.util.List;

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
