package shop.coffee.gc.controller.api;

import static java.text.MessageFormat.format;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.coffee.gc.controller.request.ProductRequest.CreateProductRequest;
import shop.coffee.gc.controller.request.ProductRequest.UpdateProductRequest;
import shop.coffee.gc.service.ProductDto;
import shop.coffee.gc.service.ProductService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductService productService;

  @PostMapping
  public ResponseEntity<ProductDto> registerProduct(
      @RequestBody CreateProductRequest createRequest) {
    var productDto = productService.registerProduct(
        createRequest.productName(),
        createRequest.price(),
        createRequest.category(),
        createRequest.description());
    var location = URI.create(format("/products/{0}", productDto.productId()));
    return ResponseEntity.created(location).body(productDto);
  }

  @GetMapping
  public ResponseEntity<List<ProductDto>> findProducts() {
    var products = productService.findAll();
    return ResponseEntity.ok(products);
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ProductDto> findProduct(@PathVariable UUID productId){
    var product = productService.findById(productId);
    return ResponseEntity.ok(product);
  }

  @PutMapping("/{productId}")
  public ResponseEntity<ProductDto> updateProduct(@PathVariable UUID productId,
      @RequestBody UpdateProductRequest updateRequest) {
    var productDto = productService.updateProduct(
        productId,
        updateRequest.productName(),
        updateRequest.price(),
        updateRequest.category(),
        updateRequest.description());
    return ResponseEntity.ok(productDto);
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<Void> deleteById(@PathVariable UUID productId) {
    productService.deleteById(productId);
    return ResponseEntity.ok().build();
  }
}