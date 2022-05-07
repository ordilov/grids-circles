package shop.coffee.gc.model.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import shop.coffee.gc.model.BaseTimeEntity;
import shop.coffee.gc.model.FormattedDateTime;

@Getter
public class Order extends BaseTimeEntity {

  private final UUID orderId;
  private final Email email;
  private String address;
  private String postcode;
  private final List<OrderItem> orderItems;
  private OrderStatus orderStatus;

  public Order(
      UUID orderId,
      Email email,
      String address,
      String postcode,
      List<OrderItem> orderItems,
      OrderStatus orderStatus,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    super(new FormattedDateTime(createdAt), new FormattedDateTime(updatedAt));
    this.orderId = orderId;
    this.email = email;
    this.address = address;
    this.postcode = postcode;
    this.orderItems = orderItems;
    this.orderStatus = orderStatus;
  }

  public void updateOrderStatus(OrderStatus orderStatus) {
    this.orderStatus = orderStatus;
    this.updatedAt = FormattedDateTime.now();
  }
}