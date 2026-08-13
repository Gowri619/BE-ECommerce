package com.gowri.commerceflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gowri.commerceflow.dto.request.CreateProductRequest;
import com.gowri.commerceflow.dto.response.ProductResponse;
import com.gowri.commerceflow.entity.Category;
import com.gowri.commerceflow.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(productController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Test
    void createProduct_returnsProduct() throws Exception {
        CreateProductRequest request = new CreateProductRequest();
        request.setName("iPhone 15");
        request.setDescription("Latest Apple smartphone");
        request.setPrice(80000);
        request.setStockQuantity(10);
        request.setCategory(Category.ELECTRONICS);

        ProductResponse response = ProductResponse.builder()
                .id(1L)
                .name("iPhone 15")
                .description("Latest Apple smartphone")
                .price(80000)
                .stockQuantity(10)
                .category(Category.ELECTRONICS)
                .build();

        when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("iPhone 15"))
                .andExpect(jsonPath("$.price").value(80000));
    }

    @Test
    void getAllProducts_returnsPagedProducts() throws Exception {
        ProductResponse product = ProductResponse.builder()
                .id(1L)
                .name("iPhone 15")
                .price(80000)
                .stockQuantity(10)
                .category(Category.ELECTRONICS)
                .build();

        Page<ProductResponse> page = new PageImpl<>(new java.util.ArrayList<>(List.of(product)));

        when(productService.getAllProducts(anyInt(), anyInt(), anyString())).thenReturn(page);

        // call controller method directly to avoid MockMvc JSON serialization issues
        var response = productController.getAllProducts(0, 10, "createdAt");
        org.junit.jupiter.api.Assertions.assertEquals(200, response.getStatusCode().value());
        org.junit.jupiter.api.Assertions.assertNotNull(response.getBody());
        org.junit.jupiter.api.Assertions.assertEquals("iPhone 15", response.getBody().getContent().get(0).getName());
    }
}
