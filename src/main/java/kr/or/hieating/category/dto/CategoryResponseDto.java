package kr.or.hieating.category.dto;

import lombok.Data;

@Data
public class CategoryResponseDto {

  private Long id;
  private String name;
  private long productCount;
}
