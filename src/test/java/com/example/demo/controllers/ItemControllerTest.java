package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void init(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void get_items_happy_path(){
        ArrayList<Item> existingItemList = new ArrayList<>();

        when(itemRepository.findAll()).thenReturn(existingItemList);

        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(existingItemList, response.getBody());
    }

    @Test
    public void get_items_by_name_happy_path(){
        String existingName = "itemName";

        List<Item> itemList = new ArrayList<>();
        Item item = new Item();
        itemList.add(item);

        when(itemRepository.findByName(existingName)).thenReturn(itemList);

        ResponseEntity<List<Item>> response = itemController.getItemsByName(existingName);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    public void get_items_by_id_happy_path(){
        Long existingId = 1L;

        Item existingItem = new Item();

        when(itemRepository.findById(existingId)).thenReturn(Optional.of(existingItem));

        ResponseEntity<Item> response = itemController.getItemById(existingId);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(existingItem, response.getBody());
    }
}
