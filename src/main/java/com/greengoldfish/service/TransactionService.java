package com.greengoldfish.service;

import com.greengoldfish.domain.Transaction;
import com.greengoldfish.domain.User;

public interface TransactionService {
    Transaction create(Transaction transaction);
}
