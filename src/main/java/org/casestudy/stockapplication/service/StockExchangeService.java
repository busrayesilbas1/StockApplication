package org.casestudy.stockapplication.service;

import org.casestudy.stockapplication.entity.Stock;
import org.casestudy.stockapplication.entity.StockExchange;
import org.casestudy.stockapplication.repository.StockExchangeRepository;
import org.casestudy.stockapplication.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class StockExchangeService {

    private final StockExchangeRepository stockExchangeRepository;
    private final StockRepository stockRepository;

    @Autowired
    public StockExchangeService(StockExchangeRepository stockExchangeRepository, StockRepository stockRepository) {
        this.stockExchangeRepository = stockExchangeRepository;
        this.stockRepository = stockRepository;
    }

    public Optional<StockExchange> getStockExchangeByName(String name) {
        return stockExchangeRepository.findByName(name);
    }

    @Transactional
    public Optional<StockExchange> addStockToStockExchange(String name, Long stockId) {
        Optional<StockExchange> stockExchangeOpt = stockExchangeRepository.findByName(name);

        if (stockExchangeOpt.isEmpty()) {
            return Optional.empty();
        }

        Optional<Stock> stockOpt = stockRepository.findById(stockId);

        if (stockOpt.isEmpty()) {
            return Optional.empty();
        }

        StockExchange stockExchange = stockExchangeOpt.get();
        Stock stock = stockOpt.get();

        stockExchange.getStocks().add(stock);
        if (stockExchange.getStocks().size() >= 5) {
            stockExchange.setLiveInMarket(true); // Stock exchange with more than 5 stocks can be live in the market
        }

        return Optional.of(stockExchangeRepository.save(stockExchange));
    }

    @Transactional
    public Optional<StockExchange> removeStockFromStockExchange(String name, Long stockId) {
        Optional<StockExchange> stockExchangeOpt = stockExchangeRepository.findByName(name);

        if (stockExchangeOpt.isEmpty()) {
            return Optional.empty();
        }

        Optional<Stock> stockOpt = stockRepository.findById(stockId);
        if (stockOpt.isEmpty()) {
            return Optional.empty();
        }

        StockExchange stockExchange = stockExchangeOpt.get();
        Stock stock = stockOpt.get();

        stockExchange.getStocks().remove(stock);
        if (stockExchange.getStocks().size() < 5) {
            stockExchange.setLiveInMarket(false); // Stock exchange with less than 5 stocks can not be live in the market
        }
        return Optional.of(stockExchangeRepository.save(stockExchange));
    }
}