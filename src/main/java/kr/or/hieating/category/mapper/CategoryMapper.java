package kr.or.hieating.category.mapper;

import java.util.List;
import kr.or.hieating.category.dto.CategoryResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CategoryMapper {

  List<CategoryResponseDto> findCategories();

  CategoryResponseDto findCategoryById(@Param("categoryId") Long categoryId);
}
