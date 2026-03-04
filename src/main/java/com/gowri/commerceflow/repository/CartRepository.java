package com.gowri.commerceflow.repository;

import com.gowri.commerceflow.entity.Cart;
import com.gowri.commerceflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(User user);
}
