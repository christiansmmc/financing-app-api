package com.greengoldfish.service.impl;

import com.greengoldfish.domain.Transaction;
import com.greengoldfish.domain.User;
import com.greengoldfish.domain.enumeration.MonthType;
import com.greengoldfish.domain.enumeration.TransactionType;
import com.greengoldfish.repository.TransactionRepository;
import com.greengoldfish.service.TransactionService;
import com.greengoldfish.service.UserService;
import com.greengoldfish.exception.BusinessException;
import com.greengoldfish.exception.enumerations.ErrorConstants;
import com.greengoldfish.web.rest.vm.TransactionSummaryVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
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
    public List<Transaction> findAll(LocalDate initialDate, LocalDate lastDate) {
        User loggedUser = userService.getLoggedUser();

        if (initialDate == null && lastDate == null) {
            return repository.findAllByUser(loggedUser);
        }

        initialDate = initialDate != null ? initialDate : LocalDate.now();
        lastDate = lastDate != null ? lastDate : LocalDate.now();

        return repository.findAllByUserAndDateIsBetween(loggedUser, initialDate, lastDate);
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

    @Override
    public TransactionSummaryVM summary(LocalDate initialDate, LocalDate lastDate, MonthType month) {
        User user = userService.getLoggedUser();

        if (month != null) {
            switch (month) {
                case CURRENT -> {
                    initialDate = LocalDate.now().withDayOfMonth(1);
                    lastDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
                }
                case LAST -> {
                    initialDate = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                    lastDate = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth());
                }
            }
        }

        BusinessException.throwIf(initialDate == null && lastDate == null, ErrorConstants.DATES_CANNOT_BE_NULL);

        List<Transaction> transactions = repository.findAllByUserAndDateIsBetween(user, initialDate, lastDate);

        BigDecimal totalOutcome = BigDecimal.ZERO;
        BigDecimal totalIncome = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            if (TransactionType.OUTCOME.equals(transaction.getType())) {
                totalOutcome = totalOutcome.add(transaction.getAmount());
            } else if (TransactionType.INCOME.equals(transaction.getType())) {
                totalIncome = totalIncome.add(transaction.getAmount());
            }
        }

        return TransactionSummaryVM
                .builder()
                .initialDate(initialDate)
                .lastDate(lastDate)
                .totalOutcome(totalOutcome)
                .totalIncome(totalIncome)
                .build();
    }
}
