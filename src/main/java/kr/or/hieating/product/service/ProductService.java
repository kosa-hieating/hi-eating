package kr.or.hieating.product.service;

import java.util.List;
import kr.or.hieating.product.dto.MostPurchasedProductResponseDto;
import kr.or.hieating.product.dto.ProductListItemResponseDto;
import kr.or.hieating.product.dto.ProductListPageResponseDto;
import kr.or.hieating.product.dto.ProductListSearchCondition;
import kr.or.hieating.product.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductMapper productMapper;

  public List<MostPurchasedProductResponseDto> findMostPurchasedProducts() {
    return productMapper.findMostPurchasedProducts();
  }

  public ProductListPageResponseDto findProductsByCategory(ProductListSearchCondition condition) {
    int totalCount = productMapper.countProductsByCategory(condition);
    int totalPages = Math.max((int) Math.ceil((double) totalCount / condition.getSize()), 1);
    List<ProductListItemResponseDto> products = productMapper.findProductsByCategory(condition);

    return new ProductListPageResponseDto(
        products, condition.getPage(), condition.getSize(), totalCount, totalPages);
  }
}
