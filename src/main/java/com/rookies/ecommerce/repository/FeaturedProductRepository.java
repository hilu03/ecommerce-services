package com.rookies.ecommerce.repository;

import com.rookies.ecommerce.entity.FeaturedProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.UUID;

public interface FeaturedProductRepository extends JpaRepository<FeaturedProduct, UUID> {

    Page<FeaturedProduct> findByStartDateBeforeAndEndDateAfter(Date start, Date end, Pageable pageable);

}
