package kr.or.hieating.product.mapper;

import java.util.List;
import kr.or.hieating.product.dto.MostPurchasedProductResponseDto;
import kr.or.hieating.product.dto.ProductListItemResponseDto;
import kr.or.hieating.product.dto.ProductListSearchCondition;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {

  List<MostPurchasedProductResponseDto> findMostPurchasedProducts();

  List<ProductListItemResponseDto> findProductsByCategory(ProductListSearchCondition condition);

  int countProductsByCategory(ProductListSearchCondition condition);
}
