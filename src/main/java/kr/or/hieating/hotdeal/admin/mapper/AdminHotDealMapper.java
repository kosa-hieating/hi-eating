package kr.or.hieating.hotdeal.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import kr.or.hieating.hotdeal.admin.dto.HotDealResponseDTO;
import java.util.List;

@Mapper
public interface AdminHotDealMapper {
    List<HotDealResponseDTO> selectManageableHotDeals();
}
