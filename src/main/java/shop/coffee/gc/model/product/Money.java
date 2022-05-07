package shop.coffee.gc.model.product;

import org.springframework.util.Assert;

public record Money(long amount) {

  public Money {
    Assert.isTrue(amount >= 0, "Money should be more than 0");
  }
}