package com.example.commercepilot.product.entity;

import com.example.commercepilot.admin.entity.Admin;
import com.example.commercepilot.category.entity.Category;
import com.example.commercepilot.config.BaseEntity;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete(columnName = "is_deleted")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 고유 id

    @Column(nullable = false, unique = true, length = 50)
    private String productName; // 상품명

    @Column(nullable = false)
    private Long price; // 가격

    private int stock; // 재고

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status; // 상태(판매중, 품절, 단종)

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin; // 상품 관련 관리자 연관관계

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category; // 상품 관련 카테고리 연관관계

    public Product(String productName, Long price, int stock, ProductStatus status, Category category, Admin admin) {
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.status = status;
        this.category = category;
        this.admin = admin;
    }

    // 수정 가능한 필드: "상품명, 카테고리, 가격, 재고"
    public void updateProduct(String productName, Category category, Long price) {
        this.productName = productName;
        this.category = category;
        this.price = price;
    }

    public void changeStock(int newstock) {
        if (this.status == ProductStatus.DISCONTINUED) {
            throw new CustomException(ErrorCode.PRODUCT_DISCONTINUED);
        }
        if (newstock < 0) {
            throw new CustomException(ErrorCode.INVALID_STOCK_AMOUNT);
        }
        this.stock = newstock;
        updateStatusByStock();
    }

    // 재고 추가 메서드
    public void increaseStock(int amount) {
        if (this.status == ProductStatus.DISCONTINUED) {
            throw new CustomException(ErrorCode.PRODUCT_DISCONTINUED);
        }
        if (amount <= 0) {
            throw new CustomException(ErrorCode.INVALID_STOCK_AMOUNT);
        }
        this.stock += amount;
        updateStatusByStock();
    }

    // 재고 감소 메서드
    public void decreaseStock(int amount) {
        if (this.status == ProductStatus.DISCONTINUED) {
            throw new CustomException(ErrorCode.PRODUCT_DISCONTINUED);
        }
        // 감소요청이 0보다 작거나 같을 경우 예외처리(증감수량은 1이상이어야해)
        if (amount <= 0) {
            throw new CustomException(ErrorCode.INVALID_STOCK_AMOUNT);
        }
        // 재고보다 감소 요청이 더 많을 경우 예외처리
        if (this.stock - amount < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_STOCK);
        }
        this.stock -= amount;
        updateStatusByStock();
    }

    // 재고에 따른 상품상태 변화 메서드
    private void updateStatusByStock() {

        // 단종 상태인 경우, 상태 변경하지 않고 단종 상태를 유지
        if (this.status == ProductStatus.DISCONTINUED) {
            return;
        }
        // 재고가 0보다 작거나 같으면 품절
        if (this.stock <= 0) {
            this.status = ProductStatus.SOLD_OUT;
        }
        // 아니면 판매중
        else {
            this.status = ProductStatus.ON_SALE;
        }
    }

    public void updateProductName(String newProductName) {
        this.productName = newProductName;
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public void changePrice(Long updatePrice) {
        this.price = updatePrice;
    }

    public void disontinued() {
        this.status = ProductStatus.DISCONTINUED;

    }
}