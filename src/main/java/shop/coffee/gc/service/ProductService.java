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

  private final ProductMapper productMapper;
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
    return productMapper.of(product);
  }

  public List<ProductDto> findAll() {
    return productRepository.findAll().stream()
        .map(productMapper::of).toList();
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
    return productMapper.of(product);
  }

  public ProductDto findById(UUID productId) {
    var product = productRepository.findById(productId)
        .orElseThrow(EntityNotFoundException::new);
    return productMapper.of(product);
  }
}