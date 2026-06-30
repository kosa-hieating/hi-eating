package kr.or.hieating.product.admin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kr.or.hieating.product.admin.dto.ProductSearchResponseDTO;
import kr.or.hieating.product.admin.mapper.AdminProductMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminProductService {

    private final AdminProductMapper adminProductMapper;

    public List<ProductSearchResponseDTO> searchProducts(String keyword, Long categoryId, String sortBy) {
        return adminProductMapper.searchProductsForHotDeal(keyword, categoryId, sortBy);
    }
}
