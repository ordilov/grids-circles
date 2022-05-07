package shop.coffee.gc.service;

import org.springframework.stereotype.Component;
import shop.coffee.gc.model.order.Order;

@Component
public class OrderMapper {

  public OrderDto of(Order order) {
    return new OrderDto(
        order.getOrderId(),
        order.getEmail().address(),
        order.getAddress(),
        order.getPostcode(),
        order.getOrderItems(),
        order.getOrderStatus(),
        order.getCreatedAt().dateTime(),
        order.getUpdatedAt().dateTime()
    );
  }
}