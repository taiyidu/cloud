package com.hmall.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@FeignClient("cart-service")
public interface CartClient {
    @DeleteMapping("carts")
    void deleteCartItems(@RequestParam("ids") Collection<Long> ids);
    @DeleteMapping("carts/{id}")
    void deleteCartItem(@PathVariable("id") Long id);

}
