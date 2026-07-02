package kr.or.hieating.statistics.admin.utils;

import static kr.or.hieating.statistics.admin.utils.StatisticsNumberUtils.defaultZero;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kr.or.hieating.statistics.admin.dto.AdminSalesChartDTO;
import kr.or.hieating.statistics.admin.dto.AdminSalesChartPointDTO;
import kr.or.hieating.statistics.admin.dto.AdminStatisticsPurchaseRowDTO;

public final class StatisticsChartUtils {

  private static final List<String> AGE_GROUP_LABELS =
      List.of("10대", "20대", "30대", "40대", "50대", "60대 이상");
  private static final List<String> PRICE_GROUP_LABELS =
      List.of(
          "~ 10,000원", "10,000 ~ 30,000원", "30,000 ~ 50,000원", "50,000 ~ 100,000원", "100,000원 ~");
  private static final List<String> CHART_COLORS =
      List.of("#ff7a35", "#ffd861", "#73c56b", "#5ab3d8", "#8d62d6", "#d38bea", "#ff9f40");

  private StatisticsChartUtils() {}

  public static AdminSalesChartDTO ageSalesChart(
      List<AdminStatisticsPurchaseRowDTO> purchases, LocalDate referenceDate) {
    Map<String, SalesBucket> buckets = initializeBuckets(AGE_GROUP_LABELS);
    for (AdminStatisticsPurchaseRowDTO purchase : purchases) {
      buckets.get(ageGroupLabel(purchase.birth(), referenceDate)).add(purchase.totalPrice());
    }
    return new AdminSalesChartDTO("나이별 매출 조회", toChartPoints(buckets));
  }

  public static AdminSalesChartDTO categorySalesChart(
      List<AdminStatisticsPurchaseRowDTO> purchases) {
    Map<String, SalesBucket> buckets = new LinkedHashMap<>();
    for (AdminStatisticsPurchaseRowDTO purchase : purchases) {
      String categoryName = purchase.categoryName() == null ? "기타" : purchase.categoryName();
      buckets
          .computeIfAbsent(categoryName, ignored -> new SalesBucket())
          .add(purchase.totalPrice());
    }
    return new AdminSalesChartDTO("카테고리별 매출 조회", toChartPoints(buckets));
  }

  public static AdminSalesChartDTO genderSalesChart(List<AdminStatisticsPurchaseRowDTO> purchases) {
    Map<String, SalesBucket> buckets = initializeBuckets(List.of("여성", "남성", "기타"));
    for (AdminStatisticsPurchaseRowDTO purchase : purchases) {
      buckets.get(genderLabel(purchase.gender())).add(purchase.totalPrice());
    }
    return new AdminSalesChartDTO("성별 매출 조회", toChartPoints(buckets));
  }

  public static AdminSalesChartDTO priceSalesChart(List<AdminStatisticsPurchaseRowDTO> purchases) {
    Map<String, SalesBucket> buckets = initializeBuckets(PRICE_GROUP_LABELS);
    for (AdminStatisticsPurchaseRowDTO purchase : purchases) {
      buckets.get(priceGroupLabel(purchase.purchasePrice())).add(purchase.totalPrice());
    }
    return new AdminSalesChartDTO("가격별 매출 조회", toChartPoints(buckets));
  }

  private static Map<String, SalesBucket> initializeBuckets(List<String> labels) {
    Map<String, SalesBucket> buckets = new LinkedHashMap<>();
    for (String label : labels) {
      buckets.put(label, new SalesBucket());
    }
    return buckets;
  }

  private static List<AdminSalesChartPointDTO> toChartPoints(Map<String, SalesBucket> buckets) {
    long totalSalesAmount = buckets.values().stream().mapToLong(SalesBucket::salesAmount).sum();
    List<AdminSalesChartPointDTO> points = new ArrayList<>();
    int colorIndex = 0;
    for (Map.Entry<String, SalesBucket> entry : buckets.entrySet()) {
      points.add(
          new AdminSalesChartPointDTO(
              entry.getKey(),
              entry.getValue().salesAmount(),
              entry.getValue().orderCount(),
              salesRate(entry.getValue().salesAmount(), totalSalesAmount),
              CHART_COLORS.get(colorIndex % CHART_COLORS.size())));
      colorIndex++;
    }
    return points;
  }

  private static int salesRate(long salesAmount, long totalSalesAmount) {
    if (totalSalesAmount == 0) {
      return 0;
    }
    return (int) Math.round(((double) salesAmount / totalSalesAmount) * 100);
  }

  private static String ageGroupLabel(LocalDate birth, LocalDate referenceDate) {
    if (birth == null) {
      return "60대 이상";
    }
    int age = Period.between(birth, referenceDate).getYears();
    if (age < 20) {
      return "10대";
    }
    if (age < 30) {
      return "20대";
    }
    if (age < 40) {
      return "30대";
    }
    if (age < 50) {
      return "40대";
    }
    if (age < 60) {
      return "50대";
    }
    return "60대 이상";
  }

  private static String genderLabel(String gender) {
    if ("FEMALE".equals(gender)) {
      return "여성";
    }
    if ("MALE".equals(gender)) {
      return "남성";
    }
    return "기타";
  }

  private static String priceGroupLabel(Long purchasePrice) {
    long price = purchasePrice == null ? 0L : purchasePrice;
    if (price <= 10_000L) {
      return "~ 10,000원";
    }
    if (price <= 30_000L) {
      return "10,000 ~ 30,000원";
    }
    if (price <= 50_000L) {
      return "30,000 ~ 50,000원";
    }
    if (price <= 100_000L) {
      return "50,000 ~ 100,000원";
    }
    return "100,000원 ~";
  }

  private static class SalesBucket {
    private long salesAmount;
    private int orderCount;

    private void add(Long amount) {
      salesAmount += defaultZero(amount);
      orderCount++;
    }

    private long salesAmount() {
      return salesAmount;
    }

    private int orderCount() {
      return orderCount;
    }
  }
}
