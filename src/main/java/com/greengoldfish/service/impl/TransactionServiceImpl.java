package com.greengoldfish.service.impl;

import com.greengoldfish.domain.Transaction;
import com.greengoldfish.domain.User;
import com.greengoldfish.repository.TransactionRepository;
import com.greengoldfish.service.TransactionService;
import com.greengoldfish.service.UserService;
import com.greengoldfish.service.exceptions.BusinessException;
import com.greengoldfish.service.exceptions.enumerations.ErrorConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    @Override
    public List<Transaction> findAll() {
        User loggedUser = userService.getLoggedUser();
        return repository.findAllByUser(loggedUser);
    }

    @Override
    public Transaction findById(Long id) {
        User loggedUser = userService.getLoggedUser();
        return repository.findByIdAndUser(id, loggedUser)
                .orElseThrow(BusinessException.notFound(Transaction.class));
    }

    @Override
    public Transaction update(Transaction transaction) {
        Transaction transactionToUpdate = findById(transaction.getId());

        transactionToUpdate.setName(transaction.getName());
        transactionToUpdate.setDescription(transaction.getDescription());
        transactionToUpdate.setAmount(transaction.getAmount());
        transactionToUpdate.setType(transaction.getType());
        transactionToUpdate.setDate(transaction.getDate());

        return repository.save(transactionToUpdate);
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = findById(id);
        repository.delete(transaction);
    }
}
