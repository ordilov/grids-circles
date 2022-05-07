package shop.coffee.gc.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseTimeEntity {

  protected final FormattedDateTime createdAt;
  protected FormattedDateTime updatedAt;
}