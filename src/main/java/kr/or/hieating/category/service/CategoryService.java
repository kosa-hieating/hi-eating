package kr.or.hieating.category.service;

import java.util.List;
import kr.or.hieating.category.dto.CategoryResponseDto;
import kr.or.hieating.category.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

  private final CategoryMapper categoryMapper;

  public List<CategoryResponseDto> findCategories() {
    return categoryMapper.findCategories();
  }

  public CategoryResponseDto findCategoryById(Long categoryId) {
    return categoryMapper.findCategoryById(categoryId);
  }
}
