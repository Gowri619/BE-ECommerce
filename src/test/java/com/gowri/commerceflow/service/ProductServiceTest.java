package com.gowri.commerceflow.service;

import com.gowri.commerceflow.dto.request.CreateProductRequest;
import com.gowri.commerceflow.dto.response.ProductResponse;
import com.gowri.commerceflow.entity.Category;
import com.gowri.commerceflow.entity.Product;
import com.gowri.commerceflow.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache productListCache;

    @InjectMocks
    private ProductService productService;

        @Test
        void getAllProducts_readsRepositoryAndStoresStableCacheValueOnCacheMiss() {
        Product product = Product.builder()
            .name("iPhone 15")
            .price(80000)
            .stockQuantity(10)
            .category(Category.ELECTRONICS)
            .active(true)
            .build();
        when(cacheManager.getCache("productList")).thenReturn(productListCache);
        when(productListCache.get(anyString())).thenReturn(null);
        when(productRepository.findByActiveTrue(any())).thenReturn(
            new PageImpl<>(List.of(product), PageRequest.of(0, 10), 1)
        );

        ProductResponse response = productService.getAllProducts(0, 10, "createdAt")
            .getContent().get(0);

        assertEquals("iPhone 15", response.getName());
        verify(productRepository).findByActiveTrue(any());
        verify(productListCache).put(eq("0:10:createdAt"), any());
        }

    @Test
    void createProduct_savesAndReturnsProduct() {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("iPhone 15");
        request.setDescription("Latest Apple smartphone");
        request.setPrice(80000);
        request.setStockQuantity(10);
        request.setCategory(Category.ELECTRONICS);

        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            product.setId(1L);
            return product;
        });

        ProductResponse response = productService.createProduct(request);

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());

        assertEquals("iPhone 15", productCaptor.getValue().getName());
        assertEquals(Category.ELECTRONICS, productCaptor.getValue().getCategory());
        assertNotNull(response);
        assertEquals("iPhone 15", response.getName());
        assertEquals(80000, response.getPrice());
    }
}
