package com.orderflow.inventory_service.service;

import com.orderflow.inventory_service.entity.Inventory;
import com.orderflow.inventory_service.event.OrderCreatedEvent;
import com.orderflow.inventory_service.exception.InsufficientInventoryException;
import com.orderflow.inventory_service.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private Inventory inventory;

    @BeforeEach
    void setUp() {

        inventory = new Inventory();
        inventory.setProductName("Wireless Keyboard");
        inventory.setAvailableQuantity(100);
    }

    @Test
    void shouldDecreaseInventoryWhenOrderIsProcessed() {

        OrderCreatedEvent event = new OrderCreatedEvent(
                10L,
                "Wireless Keyboard",
                3,
                79.99
        );

        when(inventoryRepository.findByProductName("Wireless Keyboard"))
                .thenReturn(Optional.of(inventory));

        inventoryService.processOrder(event);

        assertEquals(97, inventory.getAvailableQuantity());

        verify(inventoryRepository).save(inventory);
    }

    @Test
    void shouldThrowExceptionWhenInventoryIsInsufficient() {

        OrderCreatedEvent event = new OrderCreatedEvent(
                11L,
                "Wireless Keyboard",
                150,
                79.99
        );

        when(inventoryRepository.findByProductName("Wireless Keyboard"))
                .thenReturn(Optional.of(inventory));

        assertThrows(
                InsufficientInventoryException.class,
                () -> inventoryService.processOrder(event)
        );

        assertEquals(100, inventory.getAvailableQuantity());

        verify(inventoryRepository, never()).save(any(Inventory.class));
    }

    @Test
    void shouldNotUpdateInventoryWhenProductDoesNotExist() {

        OrderCreatedEvent event = new OrderCreatedEvent(
                12L,
                "Unknown Product",
                2,
                50.00
        );

        when(inventoryRepository.findByProductName("Unknown Product"))
                .thenReturn(Optional.empty());

        inventoryService.processOrder(event);

        verify(inventoryRepository, never()).save(any(Inventory.class));
    }
}