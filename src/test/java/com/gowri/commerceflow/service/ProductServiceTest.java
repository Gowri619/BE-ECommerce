package com.gowri.commerceflow.service;

import com.gowri.commerceflow.dto.request.CreateProductRequest;
import com.gowri.commerceflow.dto.response.ProductResponse;
import com.gowri.commerceflow.entity.Category;
import com.gowri.commerceflow.entity.Product;
import com.gowri.commerceflow.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

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
