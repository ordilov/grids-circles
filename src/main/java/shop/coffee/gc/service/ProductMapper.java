package shop.coffee.gc.service;

import org.springframework.stereotype.Component;
import shop.coffee.gc.model.product.Product;

@Component
public class ProductMapper {

  public ProductDto of(Product product) {
    return new ProductDto(
        product.getProductId(),
        product.getProductName().name(),
        product.getPrice().amount(),
        product.getCategory(),
        product.getDescription(),
        product.getCreatedAt().dateTime(),
        product.getUpdatedAt().dateTime());
  }
}