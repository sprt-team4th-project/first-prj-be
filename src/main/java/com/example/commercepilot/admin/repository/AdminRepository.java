package com.example.commercepilot.admin.repository;

import com.example.commercepilot.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long>, JpaSpecificationExecutor<Admin> {
    boolean existsByEmail(String email);
    Optional<Admin> findByEmail(String email);
}
