package com.greengoldfish.service;

import com.greengoldfish.domain.Transaction;
import com.greengoldfish.domain.enumeration.MonthType;
import com.greengoldfish.web.rest.vm.TransactionSummaryVM;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    Transaction create(Transaction transaction);

    List<Transaction> findAllByLoggedUser(@Nullable LocalDate initialDate, @Nullable LocalDate lastDate);

    Transaction findByIdAndLoggedUser(Long id);

    Transaction update(Transaction transaction);

    void delete(Long id);

    TransactionSummaryVM summary(LocalDate initialDate, LocalDate lastDate, MonthType month);
}
