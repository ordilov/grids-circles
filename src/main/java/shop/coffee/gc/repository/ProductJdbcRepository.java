package shop.coffee.gc.repository;

import static shop.coffee.gc.JdbcUtils.toLocalDateTime;
import static shop.coffee.gc.JdbcUtils.toUUID;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import shop.coffee.gc.model.product.Category;
import shop.coffee.gc.model.product.Money;
import shop.coffee.gc.model.product.Product;
import shop.coffee.gc.model.product.ProductName;

@Repository
@RequiredArgsConstructor
public class ProductJdbcRepository implements ProductRepository {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Override
  public Product insert(Product product) {
    var update = jdbcTemplate.update("""
              INSERT INTO products(product_id, product_name, category, price, description, created_at, updated_at)
            VALUES(UUID_TO_BIN(:productId), :productName, :category, :price, :description, :createdAt, :updatedAt)
        """, toParamMap(product));
    if (update != 1) {
      throw new EmptyResultDataAccessException("Nothing was inserted", 1);
    }
    return product;
  }

  @Override
  public Product update(Product product) {
    var update = jdbcTemplate.update("""
        UPDATE products
        SET product_name = :productName,
        category = :category,
        price = :price,
        description = :description,
        updated_at = :updatedAt
        WHERE product_id = UUID_TO_BIN(:productId)
        """, toParamMap(product));
    if (update != 1) {
      throw new EmptyResultDataAccessException("Nothing was updated", 1);
    }
    return product;
  }

  @Override
  public Optional<Product> findById(UUID productId) {
    try {
      return Optional.ofNullable(
          jdbcTemplate.queryForObject("""
                  SELECT *\040
                  FROM products
                  WHERE product_id = UUID_TO_BIN(:productId)
                  """,
              Collections.singletonMap("productId", productId.toString().getBytes()),
              productRowMapper));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<Product> findAll() {
    return jdbcTemplate.query("""
        SELECT * FROM products
        """, productRowMapper);
  }

  @Override
  public void deleteById(UUID productId) {
    var update = jdbcTemplate.update("""
        DELETE FROM products WHERE product_id = UUID_TO_BIN(:productId)
        """, Collections.singletonMap("productId", productId.toString().getBytes()));
  }

  private static final RowMapper<Product> productRowMapper = (resultSet, i) -> {
    var productId = toUUID(resultSet.getBytes("product_id"));
    var productName = resultSet.getString("product_name");
    var category = Category.valueOf(resultSet.getString("category"));
    var price = resultSet.getLong("price");
    var description = resultSet.getString("description");
    var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
    var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
    return new Product(productId, new ProductName(productName), category, new Money(price),
        description, createdAt, updatedAt);
  };


  private Map<String, Object> toParamMap(Product product) {
    var paramMap = new HashMap<String, Object>();
    paramMap.put("productId", product.getProductId().toString().getBytes());
    paramMap.put("productName", product.getProductName().name());
    paramMap.put("category", product.getCategory().toString());
    paramMap.put("price", product.getPrice().amount());
    paramMap.put("description", product.getDescription());
    paramMap.put("createdAt", product.getCreatedAt().dateTime().toString());
    paramMap.put("updatedAt", product.getUpdatedAt().dateTime().toString());
    return paramMap;
  }
}