package com.learning.main.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.main.enm.TransactionType;
import com.learning.main.model.TeamFinance;
import java.time.LocalDate;

import java.util.List;

public interface TeamFinanceRepository extends JpaRepository<TeamFinance, Long> {
    List<TeamFinance> findByTransactionType(TransactionType type);
    List<TeamFinance> findByRecordDateBetween(LocalDate start, LocalDate end);
    List<TeamFinance> findByAmountGreaterThan(double amount);
}
