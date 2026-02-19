package com.example.commercepilot.product.entity;

import com.example.commercepilot.config.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SoftDelete;

@Getter
@Entity
@Table(name= "products")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SoftDelete(columnName = "isdeleted")
public class Product extends BaseEntity {
    // 상품명, 카테고리, 가격, 재고, 상태( 판매중, 품절, 단종)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String productName;
    private String category;
    private Long price;
    private int stock;
    private String status;

    public Product(String productName, String category, Long price, int stock, String status) {
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }
    // 수정 가능한 필드: "상품명, 카테고리, 가격"
    public void updateProduct(String productName,String category, Long price) {
        this.productName = productName;
        this.category = category;
        this.price = price;
    }
}
