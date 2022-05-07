package shop.coffee.gc.model.order;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.regex.Pattern;
import org.springframework.util.Assert;

public record Email(String address) {

  public Email {
    Assert.notNull(address, "address should not be null");
    Assert.isTrue(address.length() >= 4 && address.length() <= 50,
        "address length must be between 4 and 50 characters");
    Assert.isTrue(checkAddress(address), "Invalid email address");
  }

  private static boolean checkAddress(String address) {
    return Pattern.matches("\\b[\\w.-]+@[\\w.-]+\\.\\w{2,4}\\b", address);
  }
}