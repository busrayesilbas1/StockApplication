package org.casestudy.stockapplication.repository;

import org.casestudy.stockapplication.entity.StockExchange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockExchangeRepository extends JpaRepository<StockExchange, Long> {
    Optional<StockExchange> findByName(String name);

    @Query("SELECT se FROM StockExchange se JOIN se.stocks s WHERE s.id = :stockId")
    List<StockExchange> findByStockId(Long stockId);
}