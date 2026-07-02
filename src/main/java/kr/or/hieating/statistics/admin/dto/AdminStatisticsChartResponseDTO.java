package kr.or.hieating.statistics.admin.dto;

import java.time.LocalDate;

public record AdminStatisticsChartResponseDTO(
    LocalDate periodStart,
    LocalDate periodEnd,
    AdminSalesChartDTO ageSalesChart,
    AdminSalesChartDTO categorySalesChart,
    AdminSalesChartDTO genderSalesChart,
    AdminSalesChartDTO priceSalesChart) {}
