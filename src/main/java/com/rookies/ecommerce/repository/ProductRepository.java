package com.rookies.ecommerce.repository;

import com.rookies.ecommerce.entity.Category;
import com.rookies.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Page<Product> findAllByIsDeleted(boolean isDeleted, PageRequest pageRequest);

    Page<Product> findAllByCategoryAndIsDeleted(Category category, boolean isDeleted, PageRequest pageRequest);

    Optional<Product> findBySlug(String slug);
}
