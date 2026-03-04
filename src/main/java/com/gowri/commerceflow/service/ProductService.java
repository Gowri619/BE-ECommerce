package com.gowri.commerceflow.service;

import com.gowri.commerceflow.dto.request.CreateProductRequest;
import com.gowri.commerceflow.dto.request.UpdateProductRequest;
import com.gowri.commerceflow.dto.response.ProductResponse;
import com.gowri.commerceflow.entity.Category;
import com.gowri.commerceflow.entity.Product;
import com.gowri.commerceflow.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductResponse createProduct(CreateProductRequest request) {

        Product product = Product.builder()
                .name((request.getName()))
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .category(request.getCategory())
                .active(true)
                .build();

        productRepository.save(product);
        return mapToResponse(product);
    }

    public Page<ProductResponse> getAllProducts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        return productRepository.findByActiveTrue(pageable).map(this::mapToResponse);
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder().id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .category(product.getCategory())
                .build();
    }

    public ProductResponse updateProduct(long id, UpdateProductRequest request) {

        Product product = productRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Product Not Found"));

        if(request.getName() != null) {
            product.setName(request.getName());
        }
        if(request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if(request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if(request.getStockQuantity() != null) {
            product.setStockQuantity(request.getStockQuantity());
        }
        if(request.getCategory() != null) {
            product.setCategory(request.getCategory());
        }
        if(request.getActive() != null) {
            product.setActive(request.getActive());
        }

        productRepository.save(product);

        return mapToResponse(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setActive(false);
        productRepository.save(product);
    }

    public Page<ProductResponse> searchProducts(
            Category category,
            Double minPrice,
            Double maxPrice,
            String keyword,
            int page, int size, String sortBy
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());

        return productRepository.searchProducts(
                category,
                minPrice,
                maxPrice,
                keyword, pageable
        ).map(this::mapToResponse);
    }
}
