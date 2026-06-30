package kr.or.hieating.product.domain;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class Products {

  private int id;
  private int categoryId;
  private String name;
  private String description;
  private int price;
  private int view_count;
  private String status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
