package kr.or.hieating.statistics.admin.dto;

import java.time.LocalDate;
import java.util.List;

public record AdminStatisticsSummaryDTO(
    List<AdminStatisticsMetricDTO> metrics, LocalDate periodStart, LocalDate periodEnd) {}
