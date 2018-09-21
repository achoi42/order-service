package com.solstice.client;

import com.solstice.model.info.ProductInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("product-service")
@Component
public interface ProductClient {

  @GetMapping("/products/{productId}")
  ProductInfo getOne(@PathVariable(value = "productId") long productId);
}
