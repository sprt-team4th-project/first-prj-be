package com.example.commercepilot.product.service;

import com.example.commercepilot.admin.entity.Admin;
import com.example.commercepilot.admin.entity.AdminRole;
import com.example.commercepilot.admin.repository.AdminRepository;
import com.example.commercepilot.category.entity.Category;
import com.example.commercepilot.category.repository.CategoryRepository;
import com.example.commercepilot.exception.CustomException;
import com.example.commercepilot.exception.ErrorCode;
import com.example.commercepilot.product.dto.request.ProductCreateRequest;
import com.example.commercepilot.product.dto.request.ProductUpdateRequest;
import com.example.commercepilot.product.dto.request.SessionAdmin;
import com.example.commercepilot.product.dto.response.ProductCreateResponse;
import com.example.commercepilot.product.dto.response.ProductUpdateResponse;
import com.example.commercepilot.product.dto.response.StockChangeResponse;
import com.example.commercepilot.product.entity.Product;
import com.example.commercepilot.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
@RequiredArgsConstructor
//쓰기전용
public class ProductCommandService {

    private final CategoryRepository categoryRepository;
    private final AdminRepository adminRepository;
    private final ProductRepository productRepository;

    // 관리자 권한 및 존재 여부 검증
    private Admin validateAdmin(SessionAdmin sessionAdmin) {
        if (sessionAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        Admin admin = adminRepository.findById(sessionAdmin.adminId()).orElseThrow(
                () -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        if (sessionAdmin.role() != AdminRole.OPERATION_ADMIN && sessionAdmin.role() != AdminRole.SUPER_ADMIN) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        return admin;
    }

    // 삭제/수정 대상 상품 존재 검증
    private Product validateProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    // 요청 카테고리 유효성 검증
    private Category validateCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public ProductCreateResponse createProduct(SessionAdmin sessionAdmin, ProductCreateRequest request) {

        Admin admin = validateAdmin(sessionAdmin);

        Category category = validateCategory(request.categoryId());

        Product product = new Product(
                request.productName(),
                request.price(),
                request.stock(),
                request.status(),
                category,
                admin
        );

        Product savedProduct = productRepository.save(product);

        return ProductCreateResponse.from(savedProduct);
    }

    public ProductUpdateResponse updateProduct(SessionAdmin sessionAdmin, ProductUpdateRequest request, Long productId) {

        validateAdmin(sessionAdmin);

        Product product = validateProduct(productId);

        Category category = null;
        if (request.categoryId() != null) {
            category = validateCategory(request.categoryId());
        }
        product.updateProduct(request.productName(), request.price(), category);

        return ProductUpdateResponse.from(product);
    }

    public void deleteProduct(SessionAdmin sessionAdmin, Long productId) {

        validateAdmin(sessionAdmin);

        validateProduct(productId);

        productRepository.deleteById(productId);
    }

    public StockChangeResponse updateStock(SessionAdmin sessionAdmin, Long productId, int newStock) {

        validateAdmin(sessionAdmin);

        Product product = validateProduct(productId);

        product.updateStock(newStock);

        return StockChangeResponse.from(product);
    }

    public StockChangeResponse increaseStock(SessionAdmin sessionAdmin, Long productId, int amount) {

        validateAdmin(sessionAdmin);

        Product product = validateProduct(productId);

        product.increaseStock(amount);

        return StockChangeResponse.from(product);
    }

    public StockChangeResponse decreaseStock(SessionAdmin sessionAdmin, Long productId, int amount) {

        validateAdmin(sessionAdmin);

        Product product = validateProduct(productId);

        product.decreaseStock(amount);

        return StockChangeResponse.from(product);
    }

    public void discontinueProduct(SessionAdmin sessionAdmin, Long productId) {
        validateAdmin(sessionAdmin);

        Product product = validateProduct(productId);

        product.disontinued();


    }
}



