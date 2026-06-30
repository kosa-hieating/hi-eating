package kr.or.hieating.product.mapper;

import java.util.List;
import kr.or.hieating.product.dto.MostPurchasedProductResponseDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper {

  List<MostPurchasedProductResponseDto> findMostPurchasedProducts();
}
