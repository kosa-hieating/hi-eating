package kr.or.hieating.statistics.admin.controller;

import static kr.or.hieating.statistics.admin.utils.StatisticsDateRangeUtils.defaultChartStartDate;

import java.time.LocalDate;
import kr.or.hieating.statistics.admin.dto.AdminStatisticsChartResponseDTO;
import kr.or.hieating.statistics.admin.dto.AdminStatisticsSummaryDTO;
import kr.or.hieating.statistics.admin.service.AdminStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/statistics")
public class AdminStatisticsController {

  private static final String CHART_JS_CDN =
      "https://cdn.jsdelivr.net/npm/chart.js@4.4.9/dist/chart.umd.min.js";

  private final AdminStatisticsService adminStatisticsService;

  @GetMapping
  public String getStatisticsPage(Model model) {
    LocalDate today = LocalDate.now();
    LocalDate periodStart = defaultChartStartDate(today);
    AdminStatisticsSummaryDTO summary = adminStatisticsService.getStatisticsSummary(today);
    AdminStatisticsChartResponseDTO chartStatistics =
        adminStatisticsService.getChartStatistics(periodStart, today);

    model.addAttribute("metrics", summary.metrics());
    model.addAttribute("periodStart", chartStatistics.periodStart());
    model.addAttribute("periodEnd", chartStatistics.periodEnd());
    model.addAttribute("ageSalesChart", chartStatistics.ageSalesChart());
    model.addAttribute("categorySalesChart", chartStatistics.categorySalesChart());
    model.addAttribute("genderSalesChart", chartStatistics.genderSalesChart());
    model.addAttribute("priceSalesChart", chartStatistics.priceSalesChart());
    model.addAttribute("contentTemplate", "admin/statistics/index");
    model.addAttribute("contentFragment", "content");
    model.addAttribute("pageStylesheet", "admin-statistics");
    model.addAttribute("pageVendorScript", CHART_JS_CDN);
    model.addAttribute("pageScript", "admin-statistics");
    return "layout/admin-base";
  }
}
