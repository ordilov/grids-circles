package shop.coffee.gc.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import shop.coffee.gc.exception.EntityNotFoundException;
import shop.coffee.gc.model.order.Email;
import shop.coffee.gc.model.order.Order;
import shop.coffee.gc.model.order.OrderItem;
import shop.coffee.gc.model.order.OrderStatus;
import shop.coffee.gc.repository.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderService {

  private final OrderMapper orderMapper;
  private final OrderRepository orderRepository;

  public OrderDto createOrder(Email email, String address, String postcode,
      List<OrderItem> orderItems) {
    Order order = new Order(
        UUID.randomUUID(),
        email,
        address,
        postcode,
        orderItems,
        OrderStatus.ACCEPTED,
        LocalDateTime.now(),
        LocalDateTime.now());

    order = orderRepository.insert(order);
    return orderMapper.of(order);
  }

  public List<OrderDto> findAll() {
    var orders = orderRepository.findAll();
    return orders.stream().map(orderMapper::of).toList();
  }

  public OrderDto updateOrder(UUID orderId, OrderStatus orderStatus) {
    var order = orderRepository.findById(orderId)
        .orElseThrow(EntityNotFoundException::new);
    order.updateOrderStatus(orderStatus);
    order = orderRepository.update(order);
    return orderMapper.of(order);
  }

  public OrderDto findById(UUID orderId) {
    var order = orderRepository.findById(orderId)
        .orElseThrow(EntityNotFoundException::new);
    return orderMapper.of(order);
  }
}