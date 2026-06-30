package kr.or.hieating.product.admin.controller;

import kr.or.hieating.global.apiPayload.exception.GeneralException;
import kr.or.hieating.product.admin.dto.ProductSearchResponseDTO;
import kr.or.hieating.product.admin.service.AdminProductService;
import kr.or.hieating.global.apiPayload.ApiResponse;
import kr.or.hieating.global.apiPayload.code.status.ErrorStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/products")
public class AdminProductController {

    private final AdminProductService adminProductService;

    @GetMapping
    public ApiResponse<List<ProductSearchResponseDTO>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "EXPIRE_ASC") String sortBy) {

        // 정렬 조건 유효성 체크
        if (!"EXPIRE_ASC".equals(sortBy) && !"STOCK_ASC".equals(sortBy)) {
            throw new GeneralException(ErrorStatus.INVALID_SORT_BY);
        }

        List<ProductSearchResponseDTO> products = adminProductService.searchProducts(keyword, categoryId, sortBy);
        return ApiResponse.onSuccess(products);
    }
}
