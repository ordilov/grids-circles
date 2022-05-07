package shop.coffee.gc.controller.request;

import java.util.List;
import shop.coffee.gc.model.order.OrderItem;
import shop.coffee.gc.model.order.OrderStatus;

public class OrderRequest {

  public record CreateOrderRequest(
      String email,
      String address,
      String postcode,
      List<OrderItem> orderItems
  ) {

  }

  public record UpdateOrderRequest(
      OrderStatus orderStatus
  ) {

  }
}