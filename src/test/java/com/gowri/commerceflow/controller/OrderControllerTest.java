package com.gowri.commerceflow.controller;

import com.gowri.commerceflow.dto.response.OrderItemResponse;
import com.gowri.commerceflow.dto.response.OrderResponse;
import com.gowri.commerceflow.entity.OrderStatus;
import com.gowri.commerceflow.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void checkout_placesOrder() throws Exception {
        when(orderService.checkout()).thenReturn("Order placed successfully. Order ID: 1001");

        mockMvc.perform(post("/api/orders/checkout"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order placed successfully. Order ID: 1001"));
    }

    @Test
    void getMyOrders_returnsOrderList() throws Exception {
        OrderResponse order = OrderResponse.builder()
                .orderId(1001L)
                .orderStatus(OrderStatus.PLACED)
                .totalAmount(160000)
                .createdAt(LocalDateTime.now())
                .items(List.of(OrderItemResponse.builder()
                        .productId(1L)
                        .productName("iPhone 15")
                        .price(80000)
                        .quantity(2)
                        .totalPrice(160000)
                        .build()))
                .build();

        when(orderService.getMyOrders()).thenReturn(List.of(order));

        mockMvc.perform(get("/api/orders/my-orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].orderId").value(1001))
                .andExpect(jsonPath("$[0].orderStatus").value("PLACED"));
    }

    @Test
    void cancelOrder_returnsSuccessMessage() throws Exception {
        doNothing().when(orderService).cancelOrder(1001L);

        mockMvc.perform(patch("/api/orders/1001/cancel"))
                .andExpect(status().isOk())
                .andExpect(content().string("Order cancelled successfully"));
    }
}
