package kr.or.hieating.hotdeal.admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import kr.or.hieating.hotdeal.admin.service.AdminHotDealService;
import kr.or.hieating.hotdeal.admin.dto.HotDealResponseDTO;
import kr.or.hieating.product.admin.service.AdminProductService;
import kr.or.hieating.product.admin.dto.CategoryResponseDTO;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/hotdeals")
public class AdminHotDealController {

    private final AdminHotDealService adminHotDealService;
    private final AdminProductService adminProductService;

    @GetMapping
    public String getHotDealsPage(Model model) {
        List<HotDealResponseDTO> hotDeals = adminHotDealService.getExistingHotDeals();
        List<CategoryResponseDTO> categories = adminProductService.getAllCategories();
        
        model.addAttribute("hotDeals", hotDeals);
        model.addAttribute("categories", categories);
        return "admin/hotdeals/manage";
    }
}
