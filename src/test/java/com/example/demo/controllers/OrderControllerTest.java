package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
    }

    @Test
    public void submit_order_happy_path(){
        String existingUserName = "test";
        User user = new User();

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        cart.setTotal(BigDecimal.valueOf(2));
        cart.setUser(user);

        user.setCart(cart);

        user.setUsername(existingUserName);
        when(userRepository.findByUsername(existingUserName)).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit(existingUserName);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(2), response.getBody().getTotal());
    }

    @Test
    public void get_orders_for_user_happy_path(){
        String existingUserName = "test";
        User user = new User();
        user.setUsername(existingUserName);

        when(userRepository.findByUsername(existingUserName)).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(new ArrayList<UserOrder>());

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(existingUserName);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(new ArrayList<UserOrder>(), response.getBody());
    }
}
