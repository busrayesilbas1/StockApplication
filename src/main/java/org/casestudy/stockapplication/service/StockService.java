package org.casestudy.stockapplication.service;

import jakarta.transaction.Transactional;
import org.casestudy.stockapplication.entity.Stock;
import org.casestudy.stockapplication.entity.StockExchange;
import org.casestudy.stockapplication.repository.StockExchangeRepository;
import org.casestudy.stockapplication.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StockService {

    private final StockRepository stockRepository;
    private final StockExchangeRepository stockExchangeRepository;

    @Autowired
    public StockService(StockRepository stockRepository, StockExchangeRepository stockExchangeRepository) {
        this.stockRepository = stockRepository;
        this.stockExchangeRepository = stockExchangeRepository;
    }

    public Stock createStock(Stock stock) {
        stock.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        try {
            return stockRepository.save(stock);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Data integrity violation: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new RuntimeException("An error occurred while creating the stock: " + e.getMessage(), e);
        }
    }

    @Transactional
    public Optional<Stock> updateStockPrice(Long id, BigDecimal newPrice) {
        Optional<Stock> stockOptional = stockRepository.findById(id);

        if (stockOptional.isEmpty()) {
            throw new NoSuchElementException("Stock not found with id: " + id);
        }

        Stock stock = stockOptional.get();
        stock.setCurrentPrice(newPrice);
        stock.setLastUpdate(new Timestamp(System.currentTimeMillis())); // Set last updated time

        return Optional.of(stockRepository.save(stock));
    }

    @Transactional
    public void deleteStock(Long stockId) {
        if (!stockRepository.existsById(stockId)) {
            throw new NoSuchElementException("Stock not found with id: " + stockId);
        }

        List<StockExchange> stockExchanges = stockExchangeRepository.findByStockId(stockId);

        // Remove the stock from each stock exchange
        stockExchanges.forEach(stockExchange -> {
            stockExchange.getStocks().removeIf(s -> s.getId().equals(stockId));
            stockExchangeRepository.save(stockExchange); // Save the updated stock exchange
        });

        stockRepository.deleteById(stockId); // Delete the stock itself
    }
}