package shop.coffee.gc.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import shop.coffee.gc.model.order.OrderItem;
import shop.coffee.gc.model.order.OrderStatus;

public record OrderDto(
    UUID orderId,
    String email,
    String address,
    String postcode,
    List<OrderItem> orderItems,
    OrderStatus orderStatus,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {

}