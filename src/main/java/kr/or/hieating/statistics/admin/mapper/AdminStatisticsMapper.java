package kr.or.hieating.statistics.admin.mapper;

import java.time.LocalDate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AdminStatisticsMapper {

  Long sumPurchaseAmount(
      @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

  Long countPurchases(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

  Long averagePurchaseAmount(
      @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
