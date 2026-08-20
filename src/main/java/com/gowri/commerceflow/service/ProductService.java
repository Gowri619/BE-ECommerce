package com.gowri.commerceflow.service;

import com.gowri.commerceflow.dto.request.CreateProductRequest;
import com.gowri.commerceflow.dto.request.UpdateProductRequest;
import com.gowri.commerceflow.dto.response.ProductResponse;
import com.gowri.commerceflow.entity.Category;
import com.gowri.commerceflow.entity.Product;
import com.gowri.commerceflow.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CacheManager cacheManager;

    @CacheEvict(value = "productList", allEntries = true)
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
        String cacheKey = page + ":" + size + ":" + sortBy;
        Cache cache = cacheManager.getCache("productList");
        ProductPageCache cachedPage = cache == null
            ? null
            : ProductPageCache.fromCachedValue(cache.get(cacheKey));
        if (cachedPage != null) {
            return cachedPage.toPage();
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
        Page<ProductResponse> productPage = productRepository.findByActiveTrue(pageable).map(this::mapToResponse);
        if (cache != null) {
            cache.put(cacheKey, ProductPageCache.from(productPage));
        }
        return productPage;
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

        @Caching(evict = {
            @CacheEvict(value = "productList", allEntries = true),
            @CacheEvict(value = "products", key = "#id")
        })
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

        @Caching(evict = {
            @CacheEvict(value = "productList", allEntries = true),
            @CacheEvict(value = "products", key = "#id")
        })
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

    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product Not Found."));

        return mapToResponse(product);
    }

    private record ProductPageCache(
            List<ProductResponse> content,
            int pageNumber,
            int pageSize,
            long totalElements
    ) {
        private static ProductPageCache from(Page<ProductResponse> page) {
            return new ProductPageCache(
                    page.getContent(),
                    page.getNumber(),
                    page.getSize(),
                    page.getTotalElements()
            );
        }

        private static ProductPageCache fromCachedValue(Object cachedValue) {
            if (cachedValue instanceof ProductPageCache pageCache) {
                return pageCache;
            }
            if (!(cachedValue instanceof Map<?, ?> cachedMap)) {
                return null;
            }

            Object rawContent = cachedMap.get("content");
            if (!(rawContent instanceof List<?> contentList)) {
                return null;
            }

            List<ProductResponse> content = contentList.stream()
                    .filter(Map.class::isInstance)
                    .map(Map.class::cast)
                    .map(ProductPageCache::toProductResponse)
                    .toList();

            return new ProductPageCache(
                    content,
                    numberValue(cachedMap.get("pageNumber"), 0).intValue(),
                    numberValue(cachedMap.get("pageSize"), content.size()).intValue(),
                    numberValue(cachedMap.get("totalElements"), content.size()).longValue()
            );
        }

        private static Number numberValue(Object value, Number fallback) {
            return value instanceof Number number ? number : fallback;
        }

        private static ProductResponse toProductResponse(Map<?, ?> cachedProduct) {
            Object category = cachedProduct.get("category");
            return ProductResponse.builder()
                    .id(((Number) cachedProduct.get("id")).longValue())
                    .name((String) cachedProduct.get("name"))
                    .description((String) cachedProduct.get("description"))
                    .price(((Number) cachedProduct.get("price")).doubleValue())
                    .stockQuantity(((Number) cachedProduct.get("stockQuantity")).intValue())
                    .category(category == null ? null : Category.valueOf(category.toString()))
                    .build();
        }

        private Page<ProductResponse> toPage() {
            return new PageImpl<>(content, PageRequest.of(pageNumber, pageSize), totalElements);
        }
    }
}
