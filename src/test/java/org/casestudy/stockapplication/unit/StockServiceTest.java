package org.casestudy.stockapplication.unit;

import org.casestudy.stockapplication.entity.Stock;
import org.casestudy.stockapplication.entity.StockExchange;
import org.casestudy.stockapplication.repository.StockExchangeRepository;
import org.casestudy.stockapplication.repository.StockRepository;
import org.casestudy.stockapplication.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private StockExchangeRepository stockExchangeRepository;

    @InjectMocks
    private StockService stockService;

    private Stock stock;
    private List<StockExchange> stockExchanges;

    @BeforeEach
    public void setUp() {
        stock = new Stock();
        stock.setId(1L);
        stock.setName("AAPL");
        stock.setCurrentPrice(BigDecimal.valueOf(150.00));
        stock.setLastUpdate(new Timestamp(System.currentTimeMillis()));

        stockExchanges = new ArrayList<>();
        StockExchange stockExchange = new StockExchange();
        stockExchange.setId(1L);
        stockExchange.setName("USD");
        stockExchange.getStocks().add(stock);
        stockExchanges.add(stockExchange);
    }

    @Test
    public void testCreateStock_Success() {
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        Stock result = stockService.createStock(stock);

        assertNotNull(result);
        assertEquals(stock.getName(), result.getName());
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    public void testCreateStockDataIntegrityViolation() {
        when(stockRepository.save(any(Stock.class))).thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            stockService.createStock(stock);
        });

        assertTrue(exception.getMessage().contains("Data integrity violation"));
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    public void testUpdateStockPrice_Success() {
        when(stockRepository.findById(1L)).thenReturn(Optional.of(stock));
        when(stockRepository.save(stock)).thenReturn(stock);

        Optional<Stock> result = stockService.updateStockPrice(1L, BigDecimal.valueOf(200.00));

        assertTrue(result.isPresent());
        assertEquals(BigDecimal.valueOf(200.00), result.get().getCurrentPrice());
        verify(stockRepository, times(1)).findById(1L);
        verify(stockRepository, times(1)).save(stock);
    }

    @Test
    public void testUpdateStockPrice_NotFound() {
        when(stockRepository.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            stockService.updateStockPrice(1L, BigDecimal.valueOf(200.00));
        });

        assertTrue(exception.getMessage().contains("Stock not found with id: 1"));
        verify(stockRepository, times(1)).findById(1L);
        verify(stockRepository, never()).save(any(Stock.class));
    }

    @Test
    public void testDeleteStock_Success() {
        when(stockRepository.existsById(1L)).thenReturn(true);
        when(stockExchangeRepository.findByStockId(1L)).thenReturn(stockExchanges);

        stockService.deleteStock(1L);

        verify(stockRepository, times(1)).existsById(1L);
        verify(stockExchangeRepository, times(1)).findByStockId(1L);
        verify(stockRepository, times(1)).deleteById(1L);
        stockExchanges.forEach(stockExchange -> verify(stockExchangeRepository, times(1)).save(stockExchange));
    }

    @Test
    public void testDeleteStock_NotFound() {
        when(stockRepository.existsById(1L)).thenReturn(false);

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            stockService.deleteStock(1L);
        });

        assertTrue(exception.getMessage().contains("Stock not found with id: 1"));
        verify(stockRepository, times(1)).existsById(1L);
        verify(stockExchangeRepository, never()).findByStockId(anyLong());
        verify(stockRepository, never()).deleteById(anyLong());
    }
}
