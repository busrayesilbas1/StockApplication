package org.casestudy.stockapplication.controller;

import org.casestudy.stockapplication.entity.Stock;
import org.casestudy.stockapplication.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/stock")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")  // Only ADMIN can create stocks
    public ResponseEntity<Stock> createStock(@RequestBody Stock stock) {
        Stock savedStock = stockService.createStock(stock);
        return ResponseEntity.ok(savedStock);
    }

    @PutMapping("/{id}/update-price")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")  // Both ADMIN and USER can update price
    public ResponseEntity<Stock> updateStockPrice(@PathVariable Long id, @RequestBody BigDecimal currentPrice) {
        return stockService.updateStockPrice(id, currentPrice)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")  // Only ADMIN can delete stocks
    public ResponseEntity<String> deleteStock(@PathVariable Long id) {
        stockService.deleteStock(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}