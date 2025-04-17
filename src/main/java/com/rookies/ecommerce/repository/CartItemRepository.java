package com.rookies.ecommerce.repository;

import com.rookies.ecommerce.entity.Cart;
import com.rookies.ecommerce.entity.CartItem;
import com.rookies.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    long countByCart(Cart cart);

    CartItem findByCartAndProduct(Cart cart, Product product);

    Page<CartItem> findByCart(Cart cart, Pageable pageable);
}
