package com.greengoldfish.repository;

import com.greengoldfish.domain.Transaction;
import com.greengoldfish.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByUser(User user);

    Optional<Transaction> findByIdAndUser(Long id, User user);
}
