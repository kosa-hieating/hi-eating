package kr.or.hieating.statistics.admin.controller;

import java.time.LocalDate;
import kr.or.hieating.global.apiPayload.ApiResponse;
import kr.or.hieating.statistics.admin.dto.AdminStatisticsChartResponseDTO;
import kr.or.hieating.statistics.admin.service.AdminStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/api/statistics")
public class AdminStatisticsRestController {

  private final AdminStatisticsService adminStatisticsService;

  @GetMapping
  public ApiResponse<AdminStatisticsChartResponseDTO> statisticsCharts(
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
          LocalDate endDate) {
    return ApiResponse.onSuccess(adminStatisticsService.getChartStatistics(startDate, endDate));
  }
}
