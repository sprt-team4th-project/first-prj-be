package com.example.commercepilot.category.repository;

import com.example.commercepilot.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT c FROM Category c LEFT JOIN FETCH c.parent",
            countQuery = "SELECT COUNT(c) FROM Category c")
    Page<Category> findAllWithParent(Pageable pageable);

    @Query(value = "SELECT c FROM Category c LEFT JOIN FETCH c.children WHERE c.id = :id")
    Optional<Category> findByIdWithChildren(@Param("id") Long id);

    @Query(value = "SELECT * FROM category WHERE is_deleted = true", nativeQuery = true)
    List<Category> findAllDeleted();

    @Query(value = "SELECT * FROM category WHERE id = :id AND is_deleted = true", nativeQuery = true)
    Optional<Category> findDeletedById(@Param("id") Long id);

    @Query(value = "SELECT * FROM category WHERE id = :id", nativeQuery = true)
    Optional<Category> findByIdIgnoreDeleted(@Param("id") Long parentId);

    boolean existsByName(String name);
}
