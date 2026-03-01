package com.example.commercepilot.product.repository;

import com.example.commercepilot.product.entity.Product;
import com.example.commercepilot.product.entity.ProductStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("""
select p
from Product p
/*
   키워드 검색
   카테고리 필터
   상태 필터
*/
where (:keyword is null or :keyword = '' or p.productName like %:keyword%)
  and (:categoryId is null or p.category.id = :categoryId)
  and (:status is null or p.status = :status)
""")
    Page<Product> search(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("status") ProductStatus status,
            Pageable pageable
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id = :id")
    Optional<Product> findByIdWithLock(@Param("id") Long id);
}