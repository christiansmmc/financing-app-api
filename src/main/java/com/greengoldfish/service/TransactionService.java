package com.greengoldfish.service;

import com.greengoldfish.domain.Transaction;
import com.greengoldfish.domain.User;

import java.util.List;

public interface TransactionService {
    Transaction create(Transaction transaction);

    List<Transaction> findAll();

    Transaction findById(Long id);

    Transaction update(Transaction transaction);

    void delete(Long id);
}
