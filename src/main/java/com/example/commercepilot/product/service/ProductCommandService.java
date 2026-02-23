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

    public ProductCreateResponse createProduct(SessionAdmin sessionAdmin, ProductCreateRequest request) {

        // 로그인 인증
        if (sessionAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 관리자 존재 검증
        Admin admin = adminRepository.findById(sessionAdmin.adminId()).orElseThrow(
                () -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        if (sessionAdmin.role() != AdminRole.OPERATION_ADMIN && sessionAdmin.role() != AdminRole.SUPER_ADMIN) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        // 카테고리 조회
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

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

        if (sessionAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        adminRepository.findById(sessionAdmin.adminId()).orElseThrow(
                () -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        if (sessionAdmin.role() != AdminRole.OPERATION_ADMIN && sessionAdmin.role() != AdminRole.SUPER_ADMIN) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        // 존재하는 상품인지 체크
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        String newProductName = request.productName();
        if (newProductName != null) { // 이름이 '들어온 경우'에만 검사/수정
            if (newProductName.isBlank()) { // 들어왔는데 공백이면 = 잘못된 요청
                throw new CustomException(ErrorCode.PRODUCT_NAME_BLANK);
            }
            product.updateProductName(newProductName);
        }

        Long changeCategoryId = request.categoryId();
        if (changeCategoryId != null) {
            Category category = categoryRepository.findById(changeCategoryId)
                    .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND));

            product.updateCategory(category);
        }

        Long updatePrice = request.price();
        if (updatePrice != null) {
            if (updatePrice < 0) {
                throw new CustomException(ErrorCode.INVALID_PRODUCT_PRICE);
            }
            product.updatePrice(updatePrice);
        }

        return ProductUpdateResponse.from(product);

    }

    public void deleteProduct(SessionAdmin sessionAdmin, Long productId) {
        if (sessionAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        adminRepository.findById(sessionAdmin.adminId()).orElseThrow(
                () -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        if (sessionAdmin.role() != AdminRole.OPERATION_ADMIN && sessionAdmin.role() != AdminRole.SUPER_ADMIN) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }

        productRepository.findById(productId).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        productRepository.deleteById(productId);
    }

    public StockChangeResponse updateStock(SessionAdmin sessionAdmin, Long productId, int newStock) {
        // 로그인 인증
        if (sessionAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        // 관리자 존재 검증
        adminRepository.findById(sessionAdmin.adminId()).orElseThrow(
                () -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        if (sessionAdmin.role() != AdminRole.OPERATION_ADMIN && sessionAdmin.role() != AdminRole.SUPER_ADMIN) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        product.updateStock(newStock);

        return StockChangeResponse.from(product);
    }

    public StockChangeResponse increaseStock(SessionAdmin sessionAdmin, Long productId, int amount) {

        if (sessionAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        adminRepository.findById(sessionAdmin.adminId()).orElseThrow(
                () -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        if (sessionAdmin.role() != AdminRole.OPERATION_ADMIN && sessionAdmin.role() != AdminRole.SUPER_ADMIN) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        product.increaseStock(amount);

        return StockChangeResponse.from(product);
    }

    public StockChangeResponse decreaseStock(SessionAdmin sessionAdmin, Long productId, int amount) {

        if (sessionAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        adminRepository.findById(sessionAdmin.adminId()).orElseThrow(
                () -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        if (sessionAdmin.role() != AdminRole.OPERATION_ADMIN && sessionAdmin.role() != AdminRole.SUPER_ADMIN) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        product.decreaseStock(amount);

        return StockChangeResponse.from(product);
    }

    public void discontinuedProduct(SessionAdmin sessionAdmin, Long productId) {

        if (sessionAdmin == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        adminRepository.findById(sessionAdmin.adminId()).orElseThrow(
                () -> new CustomException(ErrorCode.ADMIN_NOT_FOUND));

        if (sessionAdmin.role() != AdminRole.OPERATION_ADMIN && sessionAdmin.role() != AdminRole.SUPER_ADMIN) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        product.disontinued();


    }
}



