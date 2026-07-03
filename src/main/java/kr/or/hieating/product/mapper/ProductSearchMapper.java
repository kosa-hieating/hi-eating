package kr.or.hieating.product.mapper;

import java.util.List;
import kr.or.hieating.product.dto.ProductListItemResponseDto;
import kr.or.hieating.product.dto.ProductSearchCondition;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductSearchMapper {

  List<ProductListItemResponseDto> searchProducts(ProductSearchCondition condition);

  int countSearchProducts(ProductSearchCondition condition);
}
