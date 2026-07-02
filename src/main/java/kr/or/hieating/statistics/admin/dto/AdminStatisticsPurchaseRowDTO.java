package kr.or.hieating.statistics.admin.dto;

import java.time.LocalDate;

public record AdminStatisticsPurchaseRowDTO(
    Long purchaseId,
    Integer quantity,
    Long purchasePrice,
    Long totalPrice,
    String gender,
    LocalDate birth,
    String categoryName) {}
