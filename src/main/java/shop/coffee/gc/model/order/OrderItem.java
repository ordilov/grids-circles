package shop.coffee.gc.model.order;

import java.util.UUID;
import shop.coffee.gc.model.product.Category;

public record OrderItem(
    UUID productId,
    Category category,
    long price,
    int quantity
) {

}