package shop.coffee.gc.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import shop.coffee.gc.model.product.Product;

public interface ProductRepository {

  Product insert(Product product);

  Product update(Product product);

  Optional<Product> findById(UUID productId);

  List<Product> findAll();

  void deleteById(UUID productId);
}