package kr.or.hieating.statistics.admin.service;

import java.text.DecimalFormat;
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

  private LocalDate previousMonthComparableEndDate(LocalDate today, LocalDate previousMonthStart) {
    LocalDate previousMonthLastDate = previousMonthStart.plusMonths(1).minusDays(1);
    LocalDate sameElapsedDate = previousMonthStart.plusDays(today.getDayOfMonth() - 1L);
    return sameElapsedDate.isAfter(previousMonthLastDate) ? previousMonthLastDate : sameElapsedDate;
  }

  private long defaultZero(Long value) {
    return value == null ? 0L : value;
  }

  private String formatPercentComparison(String label, long currentValue, long previousValue) {
    if (previousValue == 0) {
      return currentValue == 0 ? label + " 0%" : label + " 신규";
    }

    double changeRate = ((double) (currentValue - previousValue) / previousValue) * 100;
    return label
        + " "
        + comparisonSymbol(changeRate)
        + " "
        + formatPercent(Math.abs(changeRate))
        + "%";
  }

  private String formatDifferenceComparison(
      String label, long currentValue, long previousValue, String unit) {
    long difference = currentValue - previousValue;
    return label
        + " "
        + comparisonSymbol(difference)
        + " "
        + formatInteger(Math.abs(difference))
        + unit;
  }

  private String formatInteger(long value) {
    return new DecimalFormat("#,##0").format(value);
  }

  private String formatPercent(double value) {
    return new DecimalFormat("#,##0.#").format(value);
  }

  private String comparisonSymbol(double value) {
    if (value > 0) {
      return "▲";
    }
    if (value < 0) {
      return "▼";
    }
    return "-";
  }
}
