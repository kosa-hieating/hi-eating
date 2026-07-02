package kr.or.hieating.statistics.admin.utils;

import java.time.LocalDate;

public final class StatisticsDateRangeUtils {

  private StatisticsDateRangeUtils() {}

  public static LocalDate previousMonthComparableEndDate(
      LocalDate today, LocalDate previousMonthStart) {
    LocalDate previousMonthLastDate = previousMonthStart.plusMonths(1).minusDays(1);
    LocalDate sameElapsedDate = previousMonthStart.plusDays(today.getDayOfMonth() - 1L);
    return sameElapsedDate.isAfter(previousMonthLastDate) ? previousMonthLastDate : sameElapsedDate;
  }
}
