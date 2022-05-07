package shop.coffee.gc.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.coffee.gc.exception.EntityNotFoundException;
import shop.coffee.gc.model.product.Category;
import shop.coffee.gc.model.product.Money;
import shop.coffee.gc.model.product.Product;
import shop.coffee.gc.model.product.ProductName;
import shop.coffee.gc.repository.ProductRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  public ProductDto registerProduct(
      String productName,
      Long price,
      Category category,
      String description) {
    var product = new Product(
        new ProductName(productName),
        new Money(price),
        category,
        description);
    product = productRepository.insert(product);
    return new ProductDto(
        product.getProductId(),
        product.getProductName().name(),
        product.getPrice().amount(),
        product.getCategory(),
        product.getDescription(),
        product.getCreatedAt().dateTime(),
        product.getUpdatedAt().dateTime());
  }

  public List<ProductDto> findAll() {
    return productRepository.findAll().stream()
        .map(product -> new ProductDto(product.getProductId(), product.getProductName().name(),
            product.getPrice().amount(), product.getCategory(), product.getDescription(),
            product.getCreatedAt().dateTime(), product.getUpdatedAt().dateTime())).toList();
  }

  public void deleteById(UUID productId) {
    productRepository.deleteById(productId);
  }

  public ProductDto updateProduct(
      UUID productId,
      String productName,
      long price,
      Category category,
      String description) {
    var product = productRepository.findById(productId)
        .orElseThrow(EntityNotFoundException::new);
    product.updateProduct(
        new ProductName(productName),
        new Money(price),
        category,
        description
    );
    product = productRepository.update(product);
    return new ProductDto(
        product.getProductId(),
        product.getProductName().name(),
        product.getPrice().amount(),
        product.getCategory(),
        product.getDescription(),
        product.getCreatedAt().dateTime(),
        product.getUpdatedAt().dateTime()
    );
  }

  public ProductDto findById(UUID productId) {
    var product = productRepository.findById(productId)
        .orElseThrow(EntityNotFoundException::new);
    return new ProductDto(
        product.getProductId(),
        product.getProductName().name(),
        product.getPrice().amount(),
        product.getCategory(),
        product.getDescription(),
        product.getCreatedAt().dateTime(),
        product.getUpdatedAt().dateTime()
    );
  }
}