package com.greengoldfish.service.impl;

import com.greengoldfish.domain.Transaction;
import com.greengoldfish.domain.User;
import com.greengoldfish.repository.TransactionRepository;
import com.greengoldfish.service.TransactionService;
import com.greengoldfish.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository repository;
    private final UserService userService;

    @Override
    public Transaction create(Transaction transaction) {
        User loggedUser = userService.getLoggedUser();
        LocalDate transactionDate = transaction.getDate() != null
                ? transaction.getDate()
                : LocalDate.now();

        transaction.setUser(loggedUser);
        transaction.setDate(transactionDate);
        return repository.save(transaction);
    }
}
