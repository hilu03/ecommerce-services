package com.rookies.ecommerce.repository;

import com.rookies.ecommerce.entity.Role;
import com.rookies.ecommerce.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"role"})
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"role"})
    Optional<User> findByEmailAndIsActive(String email, boolean isActive);

    Page<User> findAllByRoleAndIsActive(Role role, boolean isActive, PageRequest of);
}
