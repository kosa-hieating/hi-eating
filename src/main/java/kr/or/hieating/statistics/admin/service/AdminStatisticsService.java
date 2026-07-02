package kr.or.hieating.statistics.admin.service;

import static kr.or.hieating.statistics.admin.utils.StatisticsComparisonFormatter.formatDifferenceComparison;
import static kr.or.hieating.statistics.admin.utils.StatisticsComparisonFormatter.formatPercentComparison;
import static kr.or.hieating.statistics.admin.utils.StatisticsDateRangeUtils.previousMonthComparableEndDate;
import static kr.or.hieating.statistics.admin.utils.StatisticsNumberUtils.defaultZero;

import java.time.LocalDate;
import java.util.List;
import kr.or.hieating.statistics.admin.dto.AdminStatisticsMetricDTO;
import kr.or.hieating.statistics.admin.dto.AdminStatisticsSummaryDTO;
import kr.or.hieating.statistics.admin.mapper.AdminStatisticsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminStatisticsService {

  private final AdminStatisticsMapper adminStatisticsMapper;

  public AdminStatisticsSummaryDTO getStatisticsSummary(LocalDate baseDate) {
    LocalDate today = baseDate;
    LocalDate yesterday = today.minusDays(1);
    LocalDate currentMonthStart = today.withDayOfMonth(1);
    LocalDate previousMonthStart = currentMonthStart.minusMonths(1);
    LocalDate previousMonthEnd = previousMonthComparableEndDate(today, previousMonthStart);

    long todaySalesAmount = sumPurchaseAmount(today, today);
    long yesterdaySalesAmount = sumPurchaseAmount(yesterday, yesterday);
    long todayOrderCount = countPurchases(today, today);
    long yesterdayOrderCount = countPurchases(yesterday, yesterday);
    long currentMonthSalesAmount = sumPurchaseAmount(currentMonthStart, today);
    long previousMonthSalesAmount = sumPurchaseAmount(previousMonthStart, previousMonthEnd);
    long todayAveragePurchaseAmount = averagePurchaseAmount(today, today);
    long yesterdayAveragePurchaseAmount = averagePurchaseAmount(yesterday, yesterday);

    return new AdminStatisticsSummaryDTO(
        List.of(
            new AdminStatisticsMetricDTO(
                "일일 매출",
                todaySalesAmount,
                "원",
                formatPercentComparison("전일 대비", todaySalesAmount, yesterdaySalesAmount),
                "bi-stack"),
            new AdminStatisticsMetricDTO(
                "오늘 총 주문",
                todayOrderCount,
                "건",
                formatDifferenceComparison("전일 대비", todayOrderCount, yesterdayOrderCount, "건"),
                "bi-bag"),
            new AdminStatisticsMetricDTO(
                "한달 매출",
                currentMonthSalesAmount,
                "원",
                formatDifferenceComparison(
                    "전월 대비", currentMonthSalesAmount, previousMonthSalesAmount, "원"),
                "bi-bar-chart-line"),
            new AdminStatisticsMetricDTO(
                "평균 주문 금액",
                todayAveragePurchaseAmount,
                "원",
                formatDifferenceComparison(
                    "전일 대비", todayAveragePurchaseAmount, yesterdayAveragePurchaseAmount, "원"),
                "bi-wallet2")),
        currentMonthStart,
        today);
  }

  private long sumPurchaseAmount(LocalDate startDate, LocalDate endDate) {
    return defaultZero(adminStatisticsMapper.sumPurchaseAmount(startDate, endDate));
  }

  private long countPurchases(LocalDate startDate, LocalDate endDate) {
    return defaultZero(adminStatisticsMapper.countPurchases(startDate, endDate));
  }

  private long averagePurchaseAmount(LocalDate startDate, LocalDate endDate) {
    return defaultZero(adminStatisticsMapper.averagePurchaseAmount(startDate, endDate));
  }
}
