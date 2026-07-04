package kr.or.hieating.promotion.admin.mapper;

import java.util.List;
import kr.or.hieating.promotion.domain.Promotions;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminPromotionMapper {

  List<Promotions> selectAllPromotions();

  Promotions selectPromotionById(int id);

  List<Promotions> selectPromotionsByIdsForUpdate(@Param("ids") List<Integer> ids);

  List<Integer> selectAllPromotionIdsForUpdate();

  int selectMaxDisplayOrder();

  void insertPromotion(Promotions promotion);

  void updatePromotion(Promotions promotion);

  void updateDisplayOrder(@Param("id") int id, @Param("displayOrder") int displayOrder);

  void moveDisplayOrdersToTemporaryRange();

  void deletePromotion(int id);
}
