package shop.coffee.gc.repository;

import static shop.coffee.gc.util.JdbcUtils.toLocalDateTime;
import static shop.coffee.gc.util.JdbcUtils.toUUID;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
import shop.coffee.gc.model.order.Email;
import shop.coffee.gc.model.order.Order;
import shop.coffee.gc.model.order.OrderItem;
import shop.coffee.gc.model.order.OrderStatus;

@Repository
@RequiredArgsConstructor
public class OrderJdbcRepository implements OrderRepository {

  private final NamedParameterJdbcTemplate jdbcTemplate;

  @Override
  public Order insert(Order order) {
    jdbcTemplate.update(
        """
            INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at)
            VALUES (UUID_TO_BIN(:orderId), :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)
            """,
        toOrderParamMap(order));
    order.getOrderItems().forEach(item -> jdbcTemplate.update("""
            INSERT INTO order_items(order_id, product_id, category, price, quantity, created_at, updated_at)
            VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :category, :price, :quantity, :createdAt, :updatedAt)
            """,
        toOrderItemParamMap(order.getOrderId(), order.getCreatedAt().dateTime(),
            order.getUpdatedAt().dateTime(), item)));
    return order;
  }

  @Override
  public List<Order> findAll() {
    return jdbcTemplate.query("SELECT * FROM orders", orderRowMapper);
  }

  @Override
  public Optional<Order> findById(UUID orderId) {
    try {
      var order = jdbcTemplate.queryForObject(
          """
              SELECT * FROM orders\040
              WHERE order_id = UUID_TO_BIN(:orderId)\040
              """, Collections.singletonMap("orderId", orderId.toString().getBytes()),
          orderRowMapper);
      return Optional.ofNullable(order);
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Order update(Order order) {
    var update = jdbcTemplate.update(
        """
            UPDATE orders
            SET order_Status = :orderStatus
            WHERE order_id = UUID_TO_BIN(:orderId)
            """, toOrderParamMap(order));
    if (update != 1) {
      throw new EmptyResultDataAccessException("Nothing was updated", 1);
    }
    return order;
  }

  private static final RowMapper<Order> orderRowMapper = (resultSet, i) -> {
    var orderId = toUUID(resultSet.getBytes("order_id"));
    var email = new Email(resultSet.getString("email"));
    var address = resultSet.getString("address");
    var postcode = resultSet.getString("postcode");
    var orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
    var createdAt = toLocalDateTime(resultSet.getTimestamp("created_at"));
    var updatedAt = toLocalDateTime(resultSet.getTimestamp("updated_at"));
    return new Order(orderId, email, address, postcode, new ArrayList<>(), orderStatus, createdAt,
        updatedAt);
  };

  private Map<String, Object> toOrderParamMap(Order order) {
    var paramMap = new HashMap<String, Object>();
    paramMap.put("orderId", order.getOrderId().toString().getBytes());
    paramMap.put("email", order.getEmail().address());
    paramMap.put("address", order.getAddress());
    paramMap.put("postcode", order.getPostcode());
    paramMap.put("orderStatus", order.getOrderStatus().toString());
    paramMap.put("createdAt", order.getCreatedAt().dateTime());
    paramMap.put("updatedAt", order.getUpdatedAt().dateTime());
    return paramMap;
  }

  private Map<String, Object> toOrderItemParamMap(UUID orderId, LocalDateTime createdAt,
      LocalDateTime updatedAt, OrderItem orderItem) {
    var paramMap = new HashMap<String, Object>();
    paramMap.put("orderId", orderId.toString().getBytes());
    paramMap.put("productId", orderItem.productId().toString().getBytes());
    paramMap.put("category", orderItem.category().toString());
    paramMap.put("price", orderItem.price());
    paramMap.put("quantity", orderItem.quantity());
    paramMap.put("createdAt", createdAt);
    paramMap.put("updatedAt", updatedAt);
    return paramMap;
  }
}