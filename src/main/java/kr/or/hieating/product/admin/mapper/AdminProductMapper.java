package kr.or.hieating.product.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.or.hieating.product.admin.dto.ProductSearchResponseDTO;
import java.util.List;

@Mapper
public interface AdminProductMapper {
    List<ProductSearchResponseDTO> searchProductsForHotDeal(
        @Param("keyword") String keyword,
        @Param("categoryId") Long categoryId,
        @Param("sortBy") String sortBy
    );
}
