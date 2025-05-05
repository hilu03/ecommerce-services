package com.rookies.ecommerce.repository;

import com.rookies.ecommerce.entity.FeaturedProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface FeaturedProductRepository extends JpaRepository<FeaturedProduct, UUID> {

    @Query("""
    SELECT fp FROM FeaturedProduct fp
    WHERE fp.product.id = :productId
      AND fp.id <> :excludeId
      AND (
           (:startDate BETWEEN fp.startDate AND fp.endDate)
        OR (:endDate BETWEEN fp.startDate AND fp.endDate)
        OR (fp.startDate BETWEEN :startDate AND :endDate)
        OR (fp.endDate BETWEEN :startDate AND :endDate)
      )
    """)
    List<FeaturedProduct> findOverlappingForUpdate(
            @Param("productId") UUID productId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("excludeId") UUID excludeId
    );

    @Query("""
    SELECT fp FROM FeaturedProduct fp
    WHERE fp.product.id = :productId
      AND (
           (:startDate BETWEEN fp.startDate AND fp.endDate)
        OR (:endDate BETWEEN fp.startDate AND fp.endDate)
        OR (fp.startDate BETWEEN :startDate AND :endDate)
        OR (fp.endDate BETWEEN :startDate AND :endDate)
      )
""")
    List<FeaturedProduct> findOverlappingForCreate(
            @Param("productId") UUID productId,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Query("""
    SELECT fp FROM FeaturedProduct fp
    WHERE ((fp.startDate <= :now AND fp.endDate >= :now)
    OR (:now BETWEEN fp.startDate AND fp.endDate))
    AND (fp.product.isDeleted = false)
    """)
    Page<FeaturedProduct> findActiveFeaturedProducts(
            @Param("now") Date now,
            Pageable pageable
    );


}
