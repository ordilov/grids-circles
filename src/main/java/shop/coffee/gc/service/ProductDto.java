package shop.coffee.gc.service;

import java.time.LocalDateTime;
import java.util.UUID;
import shop.coffee.gc.model.product.Category;

public record ProductDto(
    UUID productId,
    String productName,
    Long price,
    Category category,
    String description,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {
}