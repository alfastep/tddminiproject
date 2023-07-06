package com.example.tddminiproject;

import com.example.tddminiproject.controller.OrderController;
import com.example.tddminiproject.model.Order;
import com.example.tddminiproject.repository.OrderRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc
public class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    @Test
    public void testCreateOrder() throws Exception {
        // Prepare mock order
        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress("123 Main St");
        order.setTotal(100.0);

        // Mock the repository behavior,
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenAnswer(invocation -> {
            Order savedOrder = invocation.getArgument(0);
            savedOrder.setId(1L); // Assign a unique ID to the saved order
            return savedOrder;
        });

        // Perform POST request to create a new order
        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerName\": \"John Doe\", \"shippingAddress\": \"123 Main St\", \"total\": 100.0}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }


    @Test
    public void testFindOrderById() throws Exception {
        // Create a mock Order object
        Order order = new Order();
        order.setId(1L);
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.parse("2023-07-03"));
        order.setShippingAddress("123 Main St");
        order.setTotal(100.0);

        // Mock the behavior of the orderRepository.findById() method
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        // Perform the GET request to retrieve the order by ID
        mockMvc.perform(MockMvcRequestBuilders.get("/orders/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName", Matchers.is("John Doe")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.orderDate", Matchers.is("2023-07-03")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shippingAddress", Matchers.is("123 Main St")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.is(100.0)));
    }


    @Test
    public void testUpdateOrder() throws Exception {
        // Prepare mock order
        Order order = new Order();
        order.setId(1L); // Existing order ID
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress("123 Main St");
        order.setTotal(100.0);

        // Mock the repository behavior
        Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        // Perform PUT request to update the order
        mockMvc.perform(MockMvcRequestBuilders.put("/orders/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerName\": \"Jane Doe\", \"shippingAddress\": \"456 Oak St\", \"total\": 200.0}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("Jane Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.shippingAddress").value("456 Oak St"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(200.0));
    }

    @Test
    public void testDeleteOrder() throws Exception {
        // Prepare mock order
        Order order = new Order();
        order.setId(1L); // Existing order ID
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress("123 Main St");
        order.setTotal(100.0);

        // Mock the repository behavior
        Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(order));

        // Perform DELETE request to delete the order
        mockMvc.perform(MockMvcRequestBuilders.delete("/orders/1"))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testCreateOrderWithValidationErrors() throws Exception {
        // Prepare mock order with validation errors
        Order order = new Order();
        order.setCustomerName(""); // Empty customer name
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress(""); // Empty shipping address
        order.setTotal(-100.0); // Negative total

        // Perform POST request to create a new order
        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerName\": \"\", \"shippingAddress\": \"\", \"total\": -100.0}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string(containsString("Customer name is required")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Shipping address is required")))
                .andExpect(MockMvcResultMatchers.content().string(containsString("Total must be positive")));
    }

    @Test
    public void testUpdateNonexistentOrder() throws Exception {
        // Prepare mock order
        Order order = new Order();
        order.setId(1L); // Existing order ID
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress("123 Main St");
        order.setTotal(100.0);

        // Mock the repository behavior
        Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // Perform PUT request to update the order
        mockMvc.perform(MockMvcRequestBuilders.put("/orders/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"customerName\": \"Jane Doe\", \"shippingAddress\": \"456 Oak St\", \"total\": 200.0}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDeleteNonexistentOrder() throws Exception {
        // Prepare mock order
        Order order = new Order();
        order.setId(100L); // Nonexistent order ID

        // Mock the repository behavior
        Mockito.when(orderRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        // Perform DELETE request to delete the order
        mockMvc.perform(MockMvcRequestBuilders.delete("/orders/100"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
