package com.rookies.ecommerce.repository;

import com.rookies.ecommerce.entity.Customer;
import com.rookies.ecommerce.entity.Product;
import com.rookies.ecommerce.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.security.cert.CertPathBuilder;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Page<Review> findAllByProduct(Product product, Pageable pageable);

    long countByProduct(Product product);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product = :product")
    double getAvgRatingByProduct(@Param("product") Product product);

    Page<Review> findAllByCustomer(Customer customer, Pageable pageable);

    long countByProductAndRating(Product product, int i);
}
