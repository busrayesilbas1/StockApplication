package org.casestudy.stockapplication.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.casestudy.stockapplication.config.SecurityConfig;
import org.casestudy.stockapplication.controller.StockExchangeController;
import org.casestudy.stockapplication.entity.Stock;
import org.casestudy.stockapplication.entity.StockExchange;
import org.casestudy.stockapplication.service.StockExchangeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = StockExchangeController.class)
@Import(SecurityConfig.class)
public class StockExchangeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockExchangeService stockExchangeService;

    @Autowired
    private ObjectMapper objectMapper;

    private StockExchange stockExchange;

    @BeforeEach
    public void setUp() {
        Stock stock = new Stock(1L, "AAPL", "c.", null, null, null);
        stockExchange = new StockExchange(1L, "LSE", "London Stock Exchange", false, Set.of(stock));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testGetStockExchangeByName() throws Exception {
        Mockito.when(stockExchangeService.getStockExchangeByName(anyString())).thenReturn(Optional.of(stockExchange));

        mockMvc.perform(get("/api/v1/stock-exchange/LSE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("LSE"))
                .andExpect(jsonPath("$.description").value("London Stock Exchange"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testGetStockExchangeByNameNotFound() throws Exception {
        Mockito.when(stockExchangeService.getStockExchangeByName(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/stock-exchange/LSE"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testAddStockToStockExchange() throws Exception {
        Mockito.when(stockExchangeService.addStockToStockExchange(anyString(), anyLong())).thenReturn(Optional.of(stockExchange));

        mockMvc.perform(post("/api/v1/stock-exchange/LSE/add-stock/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stocks[0].name").value("AAPL"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testAddStockToStockExchangeNotFound() throws Exception {
        Mockito.when(stockExchangeService.addStockToStockExchange(anyString(), anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/stock-exchange/LSE/add-stock/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testRemoveStockFromStockExchange() throws Exception {
        StockExchange updatedStockExchange = new StockExchange(1L, "LSE", "London Stock Exchange", false, Set.of());
        Mockito.when(stockExchangeService.removeStockFromStockExchange(anyString(), anyLong())).thenReturn(Optional.of(updatedStockExchange));

        mockMvc.perform(delete("/api/v1/stock-exchange/LSE/remove-stock/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stocks").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void testRemoveStockFromStockExchangeNotFound() throws Exception {
        Mockito.when(stockExchangeService.removeStockFromStockExchange(anyString(), anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/stock-exchange/LSE/remove-stock/1"))
                .andExpect(status().isNotFound());
    }
}
