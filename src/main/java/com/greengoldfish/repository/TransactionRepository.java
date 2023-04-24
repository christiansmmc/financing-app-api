package com.greengoldfish.repository;

import com.greengoldfish.domain.Transaction;
import com.greengoldfish.domain.User;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUser(User user);
    List<Transaction> findAllByUserAndDateIsBetween(User user, LocalDate initialDate, LocalDate lastDate);
    Optional<Transaction> findByIdAndUser(Long id, User user);
}
