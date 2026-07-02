package kr.or.hieating.statistics.admin.utils;

public final class StatisticsNumberUtils {

  private StatisticsNumberUtils() {}

  public static long defaultZero(Long value) {
    return value == null ? 0L : value;
  }
}
