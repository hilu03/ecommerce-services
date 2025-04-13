package com.rookies.ecommerce.repository;

import com.rookies.ecommerce.entity.Cart;
import com.rookies.ecommerce.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    long countByCart(Cart cart);
}
