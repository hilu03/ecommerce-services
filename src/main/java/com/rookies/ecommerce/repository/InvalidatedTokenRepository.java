package com.rookies.ecommerce.repository;

import com.rookies.ecommerce.entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, Integer> {

    boolean existsByToken(String token);

}
