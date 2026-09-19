package com.orderflow.inventory_service.repository;

import com.orderflow.inventory_service.entity.Inventory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Test
    void shouldFindInventoryByProductName() {

        Inventory inventory = new Inventory();
        inventory.setProductName("Test Keyboard");
        inventory.setAvailableQuantity(25);

        inventoryRepository.save(inventory);

        Optional<Inventory> result =
                inventoryRepository.findByProductName("Test Keyboard");

        assertTrue(result.isPresent());
        assertEquals("Test Keyboard", result.get().getProductName());
        assertEquals(25, result.get().getAvailableQuantity());
    }
}