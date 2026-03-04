package com.gowri.commerceflow.controller;

import com.gowri.commerceflow.dto.request.AddToCartRequest;
import com.gowri.commerceflow.dto.response.CartResponse;
import com.gowri.commerceflow.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody AddToCartRequest request) {
        cartService.addToCart(request);
        return ResponseEntity.ok("Product Added to Cart");
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }

    @DeleteMapping("/item/{itemId}")
    public ResponseEntity<String> removeItem(@PathVariable long itemId) {
        cartService.removeItem(itemId);
        return ResponseEntity.ok("Item removed successfully");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart() {
        cartService.clearCart();
        return ResponseEntity.ok("Cart cleared Successfully");
    }
}
