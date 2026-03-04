package com.gowri.commerceflow.repository;

import com.gowri.commerceflow.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

}
