package com.gowri.commerceflow.repository;

import com.gowri.commerceflow.entity.Order;
import com.gowri.commerceflow.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);
}
