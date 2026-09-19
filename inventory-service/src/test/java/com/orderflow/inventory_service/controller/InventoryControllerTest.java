package com.orderflow.inventory_service.controller;

import com.orderflow.inventory_service.entity.Inventory;
import com.orderflow.inventory_service.repository.InventoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InventoryRepository inventoryRepository;

    @Test
    void shouldReturnAllInventory() throws Exception {

        Inventory inventory = new Inventory();
        inventory.setProductName("Wireless Keyboard");
        inventory.setAvailableQuantity(97);

        when(inventoryRepository.findAll())
                .thenReturn(List.of(inventory));

        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productName")
                        .value("Wireless Keyboard"))
                .andExpect(jsonPath("$[0].availableQuantity")
                        .value(97));
    }

    @Test
    void shouldReturnInventoryByProductName() throws Exception {

        Inventory inventory = new Inventory();
        inventory.setProductName("Wireless Keyboard");
        inventory.setAvailableQuantity(97);

        when(inventoryRepository.findByProductName("Wireless Keyboard"))
                .thenReturn(Optional.of(inventory));

        mockMvc.perform(
                        get("/api/inventory/{productName}", "Wireless Keyboard")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName")
                        .value("Wireless Keyboard"))
                .andExpect(jsonPath("$.availableQuantity")
                        .value(97));
    }

    @Test
    void shouldReturn404WhenProductDoesNotExist() throws Exception {

        when(inventoryRepository.findByProductName("Unknown Product"))
                .thenReturn(Optional.empty());

        mockMvc.perform(
                        get("/api/inventory/{productName}", "Unknown Product")
                )
                .andExpect(status().isNotFound());
    }
}