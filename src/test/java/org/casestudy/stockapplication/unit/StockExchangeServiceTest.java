package org.casestudy.stockapplication.unit;

import org.casestudy.stockapplication.entity.Stock;
import org.casestudy.stockapplication.entity.StockExchange;
import org.casestudy.stockapplication.repository.StockExchangeRepository;
import org.casestudy.stockapplication.repository.StockRepository;
import org.casestudy.stockapplication.service.StockExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockExchangeServiceTest {

    @Mock
    private StockExchangeRepository stockExchangeRepository;

    @Mock
    private StockRepository stockRepository;

    @InjectMocks
    private StockExchangeService stockExchangeService;

    private StockExchange stockExchange;
    private Stock stock;

    @BeforeEach
    public void setUp() {
        stockExchange = new StockExchange();
        stockExchange.setName("USD");
        stockExchange.setDescription("United States Dollar");
        stockExchange.setLiveInMarket(false);

        stock = new Stock();
        stock.setId(1L);
        stock.setName("AAPL");
        stock.setDescription("Apple");
        stock.setCurrentPrice(new BigDecimal("250.00"));
        stock.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    public void testGetStockExchangeByName_Found() {
        when(stockExchangeRepository.findByName("USD")).thenReturn(Optional.ofNullable(stockExchange));

        Optional<StockExchange> result = stockExchangeService.getStockExchangeByName("USD");

        assertTrue(result.isPresent());
        assertEquals("USD", result.get().getName());
    }

    @Test
    public void testGetStockExchangeByName_NotFound() {
        when(stockExchangeRepository.findByName("LSE")).thenReturn(Optional.empty());

        Optional<StockExchange> result = stockExchangeService.getStockExchangeByName("LSE");

        assertFalse(result.isPresent());
        verify(stockExchangeRepository, times(1)).findByName("LSE");
    }

    @Test
    public void testAddStockToStockExchange_Success() {
        when(stockExchangeRepository.findByName("USD")).thenReturn(Optional.of(stockExchange));
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockExchangeRepository.save(stockExchange)).thenReturn(stockExchange);

        Optional<StockExchange> result = stockExchangeService.addStockToStockExchange("USD", 1L);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getStocks().size());
        assertTrue(result.get().getStocks().contains(stock));
        assertFalse(result.get().isLiveInMarket());
        verify(stockExchangeRepository, times(1)).findByName("USD");
        verify(stockRepository, times(1)).findById(1L);
        verify(stockExchangeRepository, times(1)).save(stockExchange);
    }

    @Test
    public void testAddStockToStockExchangeStock_ExchangeNotFound() {
        when(stockExchangeRepository.findByName("USD")).thenReturn(Optional.empty());

        Optional<StockExchange> result = stockExchangeService.addStockToStockExchange("USD", 1L);

        assertFalse(result.isPresent());
        verify(stockExchangeRepository, times(1)).findByName("USD");
        verify(stockRepository, never()).findById(anyLong());
        verify(stockExchangeRepository, never()).save(any());
    }

    @Test
    public void testAddStockToStockExchange_StockNotFound() {
        when(stockExchangeRepository.findByName("USD")).thenReturn(Optional.of(stockExchange));
        when(stockRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<StockExchange> result = stockExchangeService.addStockToStockExchange("USD", 1L);

        assertFalse(result.isPresent());
        verify(stockExchangeRepository, times(1)).findByName("USD");
        verify(stockRepository, times(1)).findById(1L);
        verify(stockExchangeRepository, never()).save(any());
    }

    @Test
    public void testRemoveStockFromStockExchange_Success() {
        stockExchange.getStocks().add(stock);

        when(stockExchangeRepository.findByName("USD")).thenReturn(Optional.of(stockExchange));
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockExchangeRepository.save(stockExchange)).thenReturn(stockExchange);

        Optional<StockExchange> result = stockExchangeService.removeStockFromStockExchange("USD", 1L);

        assertTrue(result.isPresent());
        assertEquals(0, result.get().getStocks().size());
        assertFalse(result.get().isLiveInMarket());
        verify(stockExchangeRepository, times(1)).findByName("USD");
        verify(stockRepository, times(1)).findById(1L);
        verify(stockExchangeRepository, times(1)).save(stockExchange);
    }

    @Test
    public void testRemoveStockFromStockExchangeStock_ExchangeNotFound() {
        when(stockExchangeRepository.findByName("USD")).thenReturn(Optional.empty());

        Optional<StockExchange> result = stockExchangeService.removeStockFromStockExchange("USD", 1L);

        assertFalse(result.isPresent());
        verify(stockExchangeRepository, times(1)).findByName("USD");
        verify(stockRepository, never()).findById(anyLong());
        verify(stockExchangeRepository, never()).save(any());
    }

    @Test
    public void testRemoveStockFromStockExchange_StockNotFound() {
        when(stockExchangeRepository.findByName("USD")).thenReturn(Optional.of(stockExchange));
        when(stockRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<StockExchange> result = stockExchangeService.removeStockFromStockExchange("USD", 1L);

        assertFalse(result.isPresent());
        verify(stockExchangeRepository, times(1)).findByName("USD");
        verify(stockRepository, times(1)).findById(1L);
        verify(stockExchangeRepository, never()).save(any());
    }
}
