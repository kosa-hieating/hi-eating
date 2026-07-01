package kr.or.hieating.hotdeal.admin.service;

import java.util.List;
import kr.or.hieating.hotdeal.admin.dto.HotDealResponseDTO;
import kr.or.hieating.hotdeal.admin.mapper.AdminHotDealMapper;
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
}
