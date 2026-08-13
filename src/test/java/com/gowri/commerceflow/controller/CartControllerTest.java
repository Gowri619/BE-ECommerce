package com.gowri.commerceflow.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gowri.commerceflow.dto.request.AddToCartRequest;
import com.gowri.commerceflow.dto.response.CartItemResponse;
import com.gowri.commerceflow.dto.response.CartResponse;
import com.gowri.commerceflow.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CartService cartService;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    void addToCart_returnsSuccessMessage() throws Exception {
        AddToCartRequest request = new AddToCartRequest();
        request.setProductId(1L);
        request.setQuantity(2);

        // controller will deserialize a new AddToCartRequest instance from JSON,
        // so match any AddToCartRequest when stubbing
        doNothing().when(cartService).addToCart(any(AddToCartRequest.class));

        mockMvc.perform(post("/api/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product Added to Cart"));
    }

    @Test
    void getCart_returnsCartDetails() throws Exception {
        CartResponse response = CartResponse.builder()
                .cartId(1L)
                .items(List.of(CartItemResponse.builder()
                        .productId(1L)
                        .productName("iPhone 15")
                        .price(80000.0)
                        .quantity(2)
                        .totalPrice(160000.0)
                        .build()))
            .grandTotal(160000.0)
                .build();

        when(cartService.getCart()).thenReturn(response);

        mockMvc.perform(get("/api/cart"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grandTotal").value(160000.0))
                .andExpect(jsonPath("$.items[0].productName").value("iPhone 15"));
    }

    @Test
    void removeItem_returnsSuccessMessage() throws Exception {
        doNothing().when(cartService).removeItem(anyLong());

        mockMvc.perform(delete("/api/cart/item/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Item removed successfully"));
    }
}
