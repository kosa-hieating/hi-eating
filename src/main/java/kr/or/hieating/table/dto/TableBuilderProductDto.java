package kr.or.hieating.table.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableBuilderProductDto {

  private Long productId;
  private String productName;
  private int price;
  private Integer salePrice;
  private int discountRate;
  private String pictureLocation;
  private String glbSrc;

  public boolean hasModel() {
    return glbSrc != null && !glbSrc.isBlank();
  }
}
