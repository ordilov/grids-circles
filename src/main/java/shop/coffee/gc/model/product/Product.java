package shop.coffee.gc.model.product;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import shop.coffee.gc.model.BaseTimeEntity;
import shop.coffee.gc.model.FormattedDateTime;

@Getter
public class Product extends BaseTimeEntity {

  private final UUID productId;
  private ProductName productName;
  private Money price;
  private Category category;
  private String description;

  public Product(
      UUID productId,
      ProductName productName,
      Category category,
      Money price,
      String description,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    super(new FormattedDateTime(createdAt), new FormattedDateTime(updatedAt));
    this.productId = productId;
    this.productName = productName;
    this.category = category;
    this.price = price;
    this.description = description;
  }

  public Product(
      ProductName productName,
      Money price,
      Category category,
      String description) {
    super(new FormattedDateTime(LocalDateTime.now()), new FormattedDateTime(LocalDateTime.now()));
    this.productId = UUID.randomUUID();
    this.productName = productName;
    this.price = price;
    this.category = category;
    this.description = description;
  }

  public void updateProduct(
      ProductName productName,
      Money price,
      Category category,
      String description) {
    this.productName = productName;
    this.price = price;
    this.category = category;
    this.description = description;
    this.updatedAt = new FormattedDateTime(LocalDateTime.now());
  }
}