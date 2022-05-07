package shop.coffee.gc.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import shop.coffee.gc.model.order.Order;

public interface OrderRepository {

  Order insert(Order order);

  List<Order> findAll();

  Optional<Order> findById(UUID orderId);

  Order update(Order order);
}