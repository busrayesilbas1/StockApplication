package org.casestudy.stockapplication.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.casestudy.stockapplication.entity.Stock;
import org.casestudy.stockapplication.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StockControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockService stockService;

    @Autowired
    private ObjectMapper objectMapper;

    private Stock stock;

    @BeforeEach
    public void setUp() {
        stock = new Stock();
        stock.setId(1L);
        stock.setName("AAPL");
        stock.setCurrentPrice(BigDecimal.valueOf(150.00));
        stock.setLastUpdate(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateStock() throws Exception {
        Mockito.when(stockService.createStock(any(Stock.class))).thenReturn(stock);

        mockMvc.perform(post("/api/v1/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(stock)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("AAPL"))
                .andExpect(jsonPath("$.currentPrice").value(150.00));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    public void testUpdateStockPrice() throws Exception {
        Mockito.when(stockService.updateStockPrice(anyLong(), any(BigDecimal.class))).thenReturn(Optional.of(stock));

        mockMvc.perform(put("/api/v1/stock/1/update-price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BigDecimal.valueOf(200.00))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPrice").value(150.00)); // Ensure this matches the mock's return value
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "USER"})
    public void testUpdateStockPriceNotFound() throws Exception {
        Mockito.when(stockService.updateStockPrice(anyLong(), any(BigDecimal.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/stock/1/update-price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(BigDecimal.valueOf(200.00))))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteStock() throws Exception {
        Mockito.doNothing().when(stockService).deleteStock(anyLong());

        mockMvc.perform(delete("/api/v1/stock/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteStockNotFound() throws Exception {
        Mockito.doThrow(new NoSuchElementException("Stock not found with id: 1")).when(stockService).deleteStock(anyLong());

        mockMvc.perform(delete("/api/v1/stock/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Stock not found with id: 1"));
    }
}
