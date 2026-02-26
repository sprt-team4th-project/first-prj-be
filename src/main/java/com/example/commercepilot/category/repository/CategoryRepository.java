package com.example.commercepilot.category.repository;

import com.example.commercepilot.category.entity.Category;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query(value = "SELECT c FROM Category c LEFT JOIN FETCH c.parent",
            countQuery = "SELECT COUNT(c) FROM Category c")
    Page<Category> findAllWithParent(Pageable pageable);

    @Query(value = "SELECT c FROM Category c LEFT JOIN FETCH c.parent WHERE c.id = :id")
    Optional<Category> findByIdWithParent(@Param("id") Long id);

    @Query("""
            SELECT c FROM Category c
            LEFT JOIN FETCH c.parent p
            LEFT JOIN FETCH p.children
            WHERE c.id = :id
            """)
    Optional<Category> findByIdWithParentAndChildren(@Param("id") Long id);

    @Query(value = "SELECT * FROM category WHERE is_deleted = true", nativeQuery = true)
    List<Category> findAllDeleted();

    @Query(value = "SELECT * FROM category WHERE id = :id AND is_deleted = true", nativeQuery = true)
    Optional<Category> findDeletedById(@Param("id") Long id);

    @Query(value = "SELECT * FROM category WHERE id = :id", nativeQuery = true)
    Optional<Category> findByIdIgnoreDeleted(@Param("id") Long parentId);

    boolean existsByName(String name);

    @Override
    default void delete(Category entity) {
        throw new CustomException(ErrorCode.UNSUPPORTED_OPERATION);
    }

    @Override
    default void deleteById(Long id) {
        throw new CustomException(ErrorCode.UNSUPPORTED_OPERATION);
    }

    @Override
    default void deleteAll() {
        throw new CustomException(ErrorCode.UNSUPPORTED_OPERATION);
    }

    @Query(value = """
            WITH RECURSIVE tree AS (
            SELECT id FROM category WHERE id = :rootId AND is_deleted = false
            UNION ALL
            SELECT c.id FROM category c
            INNER JOIN tree t ON c.parent_id = t.id
            WHERE c.is_deleted = false
            )
            SELECT id FROM tree
                        """, nativeQuery = true)
    List<Long> findAllDescendantIds(@Param("rootId") Long rootId);

    @Modifying
    @Query("UPDATE Category c SET c.isDeleted = true, c.deletedAt = :now WHERE c.id IN :ids")
    void softDeleteAllByIds(@Param("ids") List<Long> ids, @Param("now") LocalDateTime now);

//    부모 카테고리 복원 시, 자식 카테고리 또한 같이 복원하는 메서드
//    현재 계획 중인 스팩에서는 사용하지 않지만, 추후 바로 사용할 수 있게 구현
    @Query(value = "SELECT * FROM category WHERE parent_id = :parentId AND is_deleted = true", nativeQuery = true)
    List<Category> findDeletedChildrenByParentId(@Param("parentId") Long parentId);
}
