package shop.coffee.gc.controller.api;

import static java.text.MessageFormat.format;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.coffee.gc.controller.request.OrderRequest.CreateOrderRequest;
import shop.coffee.gc.controller.request.OrderRequest.UpdateOrderRequest;
import shop.coffee.gc.model.order.Email;
import shop.coffee.gc.model.order.Order;
import shop.coffee.gc.service.OrderDto;
import shop.coffee.gc.service.OrderService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  public ResponseEntity<Order> createOrder(
      @RequestBody CreateOrderRequest orderRequest) {
    var orderDto = orderService.createOrder(
        new Email(orderRequest.email()),
        orderRequest.address(),
        orderRequest.postcode(),
        orderRequest.orderItems()
    );
    var uri = URI.create(format("/orders/{0}", orderDto.orderId()));
    return ResponseEntity.created(uri).build();
  }

  @GetMapping
  public ResponseEntity<List<OrderDto>> findAllOrders() {
    var orderDtos = orderService.findAll();
    return ResponseEntity.ok(orderDtos);
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<OrderDto> findOrder(@PathVariable UUID orderId){
    var orderDto = orderService.findById(orderId);
    return ResponseEntity.ok(orderDto);
  }

  @PutMapping("/{orderId}")
  public ResponseEntity<OrderDto> updateOrder(
      @PathVariable UUID orderId,
      @RequestBody UpdateOrderRequest orderRequest) {
    var orderDto = orderService.updateOrder(orderId, orderRequest.orderStatus());
    return ResponseEntity.ok(orderDto);
  }
}