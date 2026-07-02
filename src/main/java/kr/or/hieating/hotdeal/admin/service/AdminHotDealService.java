package kr.or.hieating.hotdeal.admin.service;

import java.time.LocalDate;
import java.util.List;
import kr.or.hieating.global.apiPayload.code.status.ErrorStatus;
import kr.or.hieating.global.apiPayload.exception.GeneralException;
import kr.or.hieating.hotdeal.admin.dto.HotDealCreateRequestDTO;
import kr.or.hieating.hotdeal.admin.dto.HotDealUpdateRequestDTO;
import kr.or.hieating.hotdeal.admin.dto.HotDealDetailResponseDTO;
import kr.or.hieating.hotdeal.admin.dto.HotDealResponseDTO;
import kr.or.hieating.hotdeal.admin.mapper.AdminHotDealMapper;
import kr.or.hieating.hotdeal.domain.HotDealProducts;
import kr.or.hieating.hotdeal.domain.HotDeals;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminHotDealService {

  private final AdminHotDealMapper adminHotDealMapper;

  public List<HotDealResponseDTO> getExistingHotDeals() {
    return adminHotDealMapper.selectManageableHotDeals();
  }

  @Transactional
  public int createHotDeal(HotDealCreateRequestDTO request) {

    LocalDate today = LocalDate.now();

    if (request.getStartsAt().isBefore(today)) {
      throw new GeneralException(ErrorStatus.INVALID_START_DATE);
    }

    if (request.getEndsAt().isBefore(request.getStartsAt())) {
      throw new GeneralException(ErrorStatus.INVALID_END_DATE);
    }

    String status = request.getStartsAt().isAfter(today) ? "SCHEDULED" : "ACTIVE";

    HotDeals hotDeal =
        HotDeals.builder()
            .title(request.getTitle())
            .description(request.getDescription())
            .startsAt(request.getStartsAt().atStartOfDay())
            .endsAt(request.getEndsAt().atTime(23, 59, 59))
            .status(
                status) // TODO: 날짜에 따라 자동으로 상태 결정하는 로직 필요 (시작일이 되면 ACTIVE로 변경, 종료일이 지나면 EXPIRED로
            // 변경)
            .createdBy(1) // TODO: 실제 로그인한 관리자 ID로 변경 필요
            .build();

    adminHotDealMapper.insertHotDeal(hotDeal);
    int hotDealId = hotDeal.getId();

    // 할인비율 적용
    double discountMultiplier = (100 - request.getDiscountRate()) / 100.0;

    for (HotDealCreateRequestDTO.ProductItemDTO item : request.getProducts()) {
      int calculatedPrice = (int) (item.getOriginalPrice() * discountMultiplier);
      int finalHotDealPrice = (int) (Math.round(calculatedPrice / 10.0) * 10); // 10원 단위 반올림

      HotDealProducts child =
          HotDealProducts.builder()
              .hotDealId(hotDealId)
              .productOptionId(item.getProductOptionId())
              .originalPrice(item.getOriginalPrice())
              .hotDealPrice(finalHotDealPrice)
              .build();

      adminHotDealMapper.insertHotDealProduct(child);
    }

    return hotDealId;
  }

  @Transactional
  public void updateHotDeal(int id, HotDealUpdateRequestDTO request) {
    LocalDate today = LocalDate.now();

    if (request.getStartsAt().isBefore(today)) {
      throw new GeneralException(ErrorStatus.INVALID_START_DATE);
    }

    if (request.getEndsAt().isBefore(request.getStartsAt())) {
      throw new GeneralException(ErrorStatus.INVALID_END_DATE);
    }

    String status = request.getStartsAt().isAfter(today) ? "SCHEDULED" : "ACTIVE";

    HotDeals hotDeal =
        HotDeals.builder()
            .id(id)
            .title(request.getTitle())
            .description(request.getDescription())
            .startsAt(request.getStartsAt().atStartOfDay())
            .endsAt(request.getEndsAt().atTime(23, 59, 59))
            .status(status)
            .build();

    adminHotDealMapper.updateHotDeal(hotDeal);

    adminHotDealMapper.deleteHotDealProductsByHotDealId(id);

    // 전달된 새로운 상품 목록으로 핫딜 상품 재등록
    double discountMultiplier = (100 - request.getDiscountRate()) / 100.0;

    for (HotDealUpdateRequestDTO.ProductItemDTO item : request.getProducts()) {
      int calculatedPrice = (int) (item.getOriginalPrice() * discountMultiplier);
      int finalHotDealPrice = (int) (Math.round(calculatedPrice / 10.0) * 10); // 10원 단위 반올림

      HotDealProducts child =
          HotDealProducts.builder()
              .hotDealId(id)
              .productOptionId(item.getProductOptionId())
              .originalPrice(item.getOriginalPrice())
              .hotDealPrice(finalHotDealPrice)
              .build();

      adminHotDealMapper.insertHotDealProduct(child);
    }
  }

  public HotDealDetailResponseDTO getHotDealDetail(int id) {
    HotDeals hotDeal = adminHotDealMapper.selectHotDealById(id);
    if (hotDeal == null) {
      throw new GeneralException(ErrorStatus.HOT_DEAL_NOT_FOUND);
    }

    List<HotDealDetailResponseDTO.ProductItemDTO> products =
        adminHotDealMapper.selectHotDealProductsDetailByHotDealId(id);

    // 첫번째 상품 기준으로 할인율 계산
    int discountRate = 0;
    if (products != null && !products.isEmpty()) {
      HotDealDetailResponseDTO.ProductItemDTO first = products.get(0);
      if (first.getOriginalPrice() > 0 && first.getHotDealPrice() != null) {
        discountRate = 100 - (first.getHotDealPrice() * 100 / first.getOriginalPrice());
      }
    }

    return HotDealDetailResponseDTO.builder()
        .id(hotDeal.getId())
        .title(hotDeal.getTitle())
        .description(hotDeal.getDescription())
        .startsAt(hotDeal.getStartsAt())
        .endsAt(hotDeal.getEndsAt())
        .discountRate(discountRate)
        .products(products)
        .build();
  }

  @Transactional
  public void deleteHotDeal(int id) {
    HotDeals hotDeal = adminHotDealMapper.selectHotDealById(id);
    if (hotDeal == null) {
      throw new GeneralException(ErrorStatus.HOT_DEAL_NOT_FOUND);
    }
    adminHotDealMapper.softDeleteHotDeal(id);
  }
}
