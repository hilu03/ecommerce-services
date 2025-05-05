package com.rookies.ecommerce.repository;

import com.rookies.ecommerce.entity.Category;
import com.rookies.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    @EntityGraph(attributePaths = {"category"})
    Page<Product> findAllByIsDeleted(boolean isDeleted, Pageable pageable);

    Page<Product> findAllByCategoryAndIsDeleted(Category category, boolean isDeleted, Pageable pageable);

    Optional<Product> findBySlug(String slug);

    Page<Product> findByNameContainingAndIsDeleted(String name, boolean isDeleted, Pageable pageable);
}
