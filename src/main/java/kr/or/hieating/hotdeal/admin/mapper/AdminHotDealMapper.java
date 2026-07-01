package kr.or.hieating.hotdeal.admin.mapper;

import java.util.List;
import kr.or.hieating.hotdeal.admin.dto.HotDealResponseDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminHotDealMapper {
  List<HotDealResponseDTO> selectManageableHotDeals();
}
