package kr.or.hieating.promotion.admin.mapper;

import java.util.List;
import kr.or.hieating.promotion.domain.Promotions;
import org.apache.ibatis.annotations.Mapper;

/**
 * 프로모션 배너 테이블(promotions)에 접근하는 MyBatis Mapper 인터페이스입니다.
 */
@Mapper
public interface AdminPromotionMapper {

  /**
   * 등록된 모든 프로모션 배너를 노출 순서(display_order) 오름차순으로 조회합니다.
   *
   * @return 프로모션 배너 도메인 객체 리스트
   */
  List<Promotions> selectAllPromotions();

  /**
   * 특정 ID를 갖는 단일 프로모션 배너 상세 정보를 조회합니다.
   * (실제 이미지 파일 경로 획득을 위해 추가되었습니다.)
   *
   * @param id 조회할 프로모션 배너의 고유 식별자 ID
   * @return 프로모션 배너 도메인 객체
   */
  Promotions selectPromotionById(int id);

  /**
   * 등록된 프로모션 배너의 노출 순서(display_order) 값 중 최대값을 조회합니다.
   *
   * @return 현재 최대 display_order 값 (데이터가 없을 경우 0 반환)
   */
  int selectMaxDisplayOrder();

  /**
   * 새로운 프로모션 배너를 데이터베이스에 등록합니다.
   *
   * @param promotion 등록할 프로모션 배너 정보 도메인 객체
   */
  void insertPromotion(Promotions promotion);

  /**
   * 특정 프로모션 배너의 세부 정보(제목, 이미지 경로, 링크, 기간)를 수정합니다.
   *
   * @param promotion 수정할 내용을 담은 프로모션 배너 도메인 객체
   */
  void updatePromotion(Promotions promotion);

  /**
   * 특정 프로모션 배너 정보를 데이터베이스에서 삭제합니다.
   *
   * @param id 삭제할 프로모션 배너의 고유 식별자 ID
   */
  void deletePromotion(int id);
}
