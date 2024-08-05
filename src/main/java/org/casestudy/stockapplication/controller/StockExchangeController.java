package org.casestudy.stockapplication.controller;

import org.casestudy.stockapplication.entity.StockExchange;
import org.casestudy.stockapplication.service.StockExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/stock-exchange")
@PreAuthorize("hasRole('ADMIN')")  // Requires ADMIN role for all methods in this controller
public class StockExchangeController {

    private final StockExchangeService stockExchangeService;

    @Autowired
    public StockExchangeController(StockExchangeService stockExchangeService) {
        this.stockExchangeService = stockExchangeService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<StockExchange> getStockExchange(@PathVariable String name) {
        return stockExchangeService.getStockExchangeByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{name}/add-stock/{stockId}")
    public ResponseEntity<StockExchange> addStockToStockExchange(@PathVariable String name, @PathVariable Long stockId) {
        return stockExchangeService.addStockToStockExchange(name, stockId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{name}/remove-stock/{stockId}")
    public ResponseEntity<StockExchange> removeStockFromStockExchange(@PathVariable String name, @PathVariable Long stockId) {
        return stockExchangeService.removeStockFromStockExchange(name, stockId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}