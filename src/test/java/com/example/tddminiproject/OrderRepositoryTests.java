package com.example.tddminiproject;

import com.example.tddminiproject.model.Order;
import com.example.tddminiproject.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class OrderRepositoryTests {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void testSaveOrder() {
        // Create a new Order object
        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress("123 Main St");
        order.setTotal(100.0);

        // Save the Order object to the repository
        Order savedOrder = orderRepository.save(order);

        // Check if the Order object was saved successfully
        assertNotNull(savedOrder.getId());
        assertEquals("John Doe", savedOrder.getCustomerName());
        assertEquals(LocalDate.now(), savedOrder.getOrderDate());
        assertEquals("123 Main St", savedOrder.getShippingAddress());
        assertEquals(100.0, savedOrder.getTotal());
    }

    @Test
    public void testFindById() {
        // Create a new Order object
        Order order = new Order();
        order.setCustomerName("Jane Smith");
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress("456 Oak St");
        order.setTotal(150.0);

        // Save the Order object to the repository
        Order savedOrder = orderRepository.save(order);

        // Retrieve the Order by ID from the repository
        Optional<Order> retrievedOrder = orderRepository.findById(savedOrder.getId());

        // Check if the Order was retrieved successfully
        assertTrue(retrievedOrder.isPresent());
        assertEquals(savedOrder.getId(), retrievedOrder.get().getId());
        assertEquals("Jane Smith", retrievedOrder.get().getCustomerName());
        assertEquals(LocalDate.now(), retrievedOrder.get().getOrderDate());
        assertEquals("456 Oak St", retrievedOrder.get().getShippingAddress());
        assertEquals(150.0, retrievedOrder.get().getTotal());
    }

    @Test
    public void testUpdateOrder() {
        // Create a new Order object
        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress("123 Main St");
        order.setTotal(100.0);

        // Save the Order object to the repository
        Order savedOrder = orderRepository.save(order);

        // Update the Order object
        savedOrder.setCustomerName("Jane Smith");
        savedOrder.setTotal(200.0);

        // Save the updated Order to the repository
        Order updatedOrder = orderRepository.save(savedOrder);

        // Retrieve the updated Order from the repository
        Optional<Order> retrievedOrder = orderRepository.findById(updatedOrder.getId());

        // Check if the Order was updated successfully
        assertTrue(retrievedOrder.isPresent());
        assertEquals(updatedOrder.getId(), retrievedOrder.get().getId());
        assertEquals("Jane Smith", retrievedOrder.get().getCustomerName());
        assertEquals(200.0, retrievedOrder.get().getTotal());
    }

    @Test
    public void testDeleteOrder() {
        // Create a new Order object
        Order order = new Order();
        order.setCustomerName("John Doe");
        order.setOrderDate(LocalDate.now());
        order.setShippingAddress("123 Main St");
        order.setTotal(100.0);

        // Save the Order object to the repository
        Order savedOrder = orderRepository.save(order);

        // Delete the Order from the repository
        orderRepository.delete(savedOrder);

        // Retrieve the Order by ID from the repository
        Optional<Order> retrievedOrder = orderRepository.findById(savedOrder.getId());

        // Check if the Order was deleted successfully
        assertFalse(retrievedOrder.isPresent());
    }
}

