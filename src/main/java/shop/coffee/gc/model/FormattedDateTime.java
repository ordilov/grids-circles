package shop.coffee.gc.model;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public record FormattedDateTime(LocalDateTime dateTime) {

  public FormattedDateTime(LocalDateTime dateTime) {
    this.dateTime = dateTime.truncatedTo(ChronoUnit.SECONDS);
  }

  public static FormattedDateTime now(){
    return new FormattedDateTime(LocalDateTime.now());
  }
}