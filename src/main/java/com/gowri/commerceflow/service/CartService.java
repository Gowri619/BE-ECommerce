package com.gowri.commerceflow.service;

import com.gowri.commerceflow.dto.request.AddToCartRequest;
import com.gowri.commerceflow.dto.response.CartItemResponse;
import com.gowri.commerceflow.dto.response.CartResponse;
import com.gowri.commerceflow.entity.Cart;
import com.gowri.commerceflow.entity.CartItem;
import com.gowri.commerceflow.entity.Product;
import com.gowri.commerceflow.entity.User;
import com.gowri.commerceflow.repository.CartItemRepository;
import com.gowri.commerceflow.repository.CartRepository;
import com.gowri.commerceflow.repository.ProductRepository;
import com.gowri.commerceflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public void addToCart(AddToCartRequest request) {
        User user = getCurrentUser();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product Not found"));

        if(!product.isActive()) {
            throw new RuntimeException("Product Not Available");
        }

        if(product.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient Stock");
        }

        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(Cart.builder().user(user).build()));

        Optional<CartItem> exitingItem = cart.getItems().stream()
                .filter(cartItem -> cartItem.getProduct().getId() == product.getId())
                .findFirst();

        if(exitingItem.isPresent()) {
            CartItem item = exitingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            CartItem newItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();

            cart.getItems().add((newItem));
        }

        cartRepository.save(cart);
    }

    @Transactional(readOnly = true)
    public CartResponse getCart() {

        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItemResponse> itemResponses = cart.getItems().stream()
                .map(cartItem -> CartItemResponse.builder()
                        .productId(cartItem.getProduct().getId())
                        .productName(cartItem.getProduct().getName())
                        .price(cartItem.getProduct().getPrice())
                        .quantity(cartItem.getQuantity())
                        .totalPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity())
                        .build()
                ).toList();

        double gradTotal = itemResponses.stream()
                .mapToDouble(CartItemResponse::totalPrice)
                .sum();

        return CartResponse.builder()
                .cartId(cart.getId())
                .items(itemResponses)
                .grandTotal(gradTotal)
                .build();
    }

    @Transactional
    public void removeItem(long itemId) {

        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("User not found"));

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item Not found"));

        if(cartItem.getCart().getId() != cart.getId()) {
            throw new RuntimeException("Unauthorized action");
        }

        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
    }

    @Transactional
    public void clearCart() {

        User user = getCurrentUser();

        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("User Not found"));

        cart.getItems().clear();
    }

}
