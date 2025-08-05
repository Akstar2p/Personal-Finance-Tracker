package com.financetracker.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.financetracker.entities.Transaction;
import com.financetracker.enums.TransactionType;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
        SELECT COALESCE(SUM(CASE WHEN t.type = 'INCOME' THEN t.amount ELSE 0 END),0)
             - COALESCE(SUM(CASE WHEN t.type = 'EXPENSE' THEN t.amount ELSE 0 END),0)
        FROM Transaction t
        WHERE t.user.id = :userId
    """)
    BigDecimal calculateBalance(@Param("userId") Long userId);

    List<Transaction> findByUserIdOrderByDateDesc(Long userId);
    
    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM Transaction t
            WHERE t.user.id = :userId
              AND t.category.id = :categoryId
              AND t.type = :type
              AND t.date >= :start
              AND t.date < :end
        """)
        Optional<BigDecimal> sumAmountByUserCategoryAndPeriod(
                @Param("userId") Long userId,
                @Param("categoryId") Long categoryId,
                @Param("type") TransactionType type,
                @Param("start") LocalDate start,
                @Param("end") LocalDate end);
}
