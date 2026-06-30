package kr.or.hieating.product.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import kr.or.hieating.product.admin.dto.ProductSearchResponseDTO;
import kr.or.hieating.product.admin.dto.CategoryResponseDTO;
import java.util.List;

@Mapper
public interface AdminProductMapper {
    List<ProductSearchResponseDTO> searchProductsForHotDeal(
        @Param("keyword") String keyword,
        @Param("categoryId") Long categoryId,
        @Param("sortBy") String sortBy,
        @Param("offset") int offset,
        @Param("size") int size
    );

    int countProductsForHotDeal(
        @Param("keyword") String keyword,
        @Param("categoryId") Long categoryId
    );

    List<CategoryResponseDTO> selectAllCategories();
}
