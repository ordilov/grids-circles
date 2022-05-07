package shop.coffee.gc.controller.request;

import shop.coffee.gc.model.product.Category;

public class ProductRequest {

  public record CreateProductRequest(
      String productName,
      Category category,
      long price,
      String description
  ) {

  }

  public record UpdateProductRequest(
      String productName,
      Category category,
      long price,
      String description
  ) {

  }
}