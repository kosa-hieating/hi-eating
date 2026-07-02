package kr.or.hieating.promotion.admin.service;

import java.util.List;

import kr.or.hieating.promotion.admin.mapper.AdminPromotionMapper;
import kr.or.hieating.promotion.domain.Promotions;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminPromotionService {

  private final AdminPromotionMapper adminPromotionMapper;

  public List<Promotions> getAllPromotions() {
    List<Promotions> promotions = adminPromotionMapper.selectAllPromotions();
    return promotions != null ? promotions : List.of();
  }
}
