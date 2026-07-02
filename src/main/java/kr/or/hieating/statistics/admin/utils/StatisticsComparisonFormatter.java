package kr.or.hieating.statistics.admin.utils;

import java.text.DecimalFormat;

public final class StatisticsComparisonFormatter {

  private StatisticsComparisonFormatter() {}

  public static String formatPercentComparison(
      String label, long currentValue, long previousValue) {
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

  public static String formatDifferenceComparison(
      String label, long currentValue, long previousValue, String unit) {
    long difference = currentValue - previousValue;
    return label
        + " "
        + comparisonSymbol(difference)
        + " "
        + formatInteger(Math.abs(difference))
        + unit;
  }

  private static String formatInteger(long value) {
    return new DecimalFormat("#,##0").format(value);
  }

  private static String formatPercent(double value) {
    return new DecimalFormat("#,##0.#").format(value);
  }

  private static String comparisonSymbol(double value) {
    if (value > 0) {
      return "▲";
    }
    if (value < 0) {
      return "▼";
    }
    return "-";
  }
}
