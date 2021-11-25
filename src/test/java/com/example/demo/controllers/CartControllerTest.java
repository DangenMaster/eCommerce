package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private CartRepository cartRepository = mock(CartRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);
    }

    @Test
    public void add_to_cart_happy_path() {
        String existingUsername = "user";
        User user = new User();
        user.setUsername(existingUsername);

        Long existingItemId = 1L;
        Item item = new Item();
        item.setId(existingItemId);
        item.setPrice(BigDecimal.valueOf(1.5));

        Cart cart = new Cart();
        user.setCart(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();

        modifyCartRequest.setUsername(existingUsername);

        modifyCartRequest.setItemId(existingItemId);
        modifyCartRequest.setQuantity(2);

        when(userRepository.findByUsername(existingUsername)).thenReturn(user);
        when(itemRepository.findById(existingItemId)).thenReturn(Optional.of(item));

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(3.0), response.getBody().getTotal());
    }

    @Test
    public void remove_from_cart_happy_path(){
        String existingUsername = "user";
        User user = new User();
        user.setUsername(existingUsername);

        Long existingItemId = 1L;
        Item item = new Item();
        item.setId(existingItemId);
        item.setPrice(BigDecimal.valueOf(1.5));

        when(userRepository.findByUsername(existingUsername)).thenReturn(user);
        when(itemRepository.findById(existingItemId)).thenReturn(Optional.of(item));

        Cart cart = new Cart();
        user.setCart(cart);

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(existingUsername);
        modifyCartRequest.setItemId(existingItemId);

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }
}
