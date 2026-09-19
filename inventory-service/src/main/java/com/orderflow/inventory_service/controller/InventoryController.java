package com.orderflow.inventory_service.controller;

import com.orderflow.inventory_service.entity.Inventory;
import com.orderflow.inventory_service.repository.InventoryRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryRepository inventoryRepository;

    public InventoryController(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventory() {

        return ResponseEntity.ok(
                inventoryRepository.findAll()
        );
    }

    @GetMapping("/{productName}")
    public ResponseEntity<Inventory> getInventoryByProductName(
            @PathVariable String productName) {

        return inventoryRepository.findByProductName(productName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}