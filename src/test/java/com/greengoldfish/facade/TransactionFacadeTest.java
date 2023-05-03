package com.greengoldfish.facade;

import com.greengoldfish.domain.Transaction;
import com.greengoldfish.domain.enumeration.MonthType;
import com.greengoldfish.facade.dto.transaction.TransactionDTO;
import com.greengoldfish.facade.dto.transaction.TransactionIdDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToCreateDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToGetDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToUpdateDTO;
import com.greengoldfish.facade.mapper.TransactionMapper;
import com.greengoldfish.service.TransactionService;
import com.greengoldfish.web.rest.vm.TransactionSummaryVM;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class TransactionFacadeTest {

    private TransactionFacade facade;

    Faker faker = new Faker();

    @Mock
    private TransactionService transactionService;
    @Mock
    private TransactionMapper transactionMapper;

    @BeforeEach
    void setUp() {
        facade = new TransactionFacade(
                transactionMapper,
                transactionService
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
        TransactionToCreateDTO dto = new TransactionToCreateDTO();
        Transaction transaction = new Transaction();
        TransactionDTO transactionDTO = new TransactionDTO();

        Mockito
                .when(transactionMapper.toEntity(dto))
                .thenReturn(transaction);
        Mockito
                .when(transactionService.create(transaction))
                .thenReturn(transaction);
        Mockito
                .when(transactionMapper.toDto(transaction))
                .thenReturn(transactionDTO);

        facade.create(dto);

        Mockito
                .verify(transactionMapper, Mockito.times(1))
                .toEntity(dto);
        Mockito
                .verify(transactionService, Mockito.times(1))
                .create(transaction);
        Mockito
                .verify(transactionMapper, Mockito.times(1))
                .toDto(transaction);

    }

    @Test
    void findAllByLoggedUser() {
        LocalDate initialDate = LocalDate.now();
        LocalDate lastDate = LocalDate.now();
        TransactionToGetDTO toGetDTO = new TransactionToGetDTO();
        Transaction transaction_01 = new Transaction();
        Transaction transaction_02 = new Transaction();
        List<Transaction> transactions = List.of(transaction_01, transaction_02);

        Mockito
                .when(transactionService.findAllByLoggedUser(initialDate, lastDate))
                .thenReturn(transactions);
        Mockito
                .when(transactionMapper.toGetDto(Mockito.any(Transaction.class)))
                .thenReturn(toGetDTO);

        facade.findAllByLoggedUser(initialDate, lastDate);

        Mockito
                .verify(transactionService, Mockito.times(1))
                .findAllByLoggedUser(initialDate, lastDate);
        Mockito
                .verify(transactionMapper, Mockito.times(transactions.size()))
                .toGetDto(Mockito.any(Transaction.class));
    }

    @Test
    void findById() {
        Long id = faker.number().randomNumber();
        Transaction transaction = new Transaction();
        TransactionToGetDTO toGetDTO = new TransactionToGetDTO();

        Mockito
                .when(transactionService.findByIdAndLoggedUser(id))
                .thenReturn(transaction);
        Mockito
                .when(transactionMapper.toGetDto(transaction))
                .thenReturn(toGetDTO);

        facade.findById(id);

        Mockito
                .verify(transactionService, Mockito.times(1))
                .findByIdAndLoggedUser(id);
        Mockito
                .verify(transactionMapper, Mockito.times(1))
                .toGetDto(transaction);
    }

    @Test
    void update() {
        Transaction transaction = new Transaction();
        TransactionToUpdateDTO toUpdateDTO = new TransactionToUpdateDTO();
        TransactionIdDTO idDTO = new TransactionIdDTO();

        Mockito
                .when(transactionMapper.toEntity(toUpdateDTO))
                .thenReturn(transaction);
        Mockito
                .when(transactionService.update(transaction))
                .thenReturn(transaction);
        Mockito
                .when(transactionMapper.toIdDto(transaction))
                .thenReturn(idDTO);


        facade.update(toUpdateDTO);

        Mockito
                .verify(transactionMapper, Mockito.times(1))
                .toEntity(toUpdateDTO);
        Mockito
                .verify(transactionService, Mockito.times(1))
                .update(transaction);
        Mockito
                .verify(transactionMapper, Mockito.times(1))
                .toIdDto(transaction);
    }

    @Test
    void delete() {
        Long id = faker.number().randomNumber();

        Mockito
                .doNothing()
                .when(transactionService)
                .delete(id);

        facade.delete(id);

        Mockito
                .verify(transactionService, Mockito.times(1))
                .delete(id);
    }

    @Test
    void summary() {
        LocalDate initialDate = LocalDate.now();
        LocalDate lastDate = LocalDate.now();
        MonthType monthType = MonthType.CURRENT;
        TransactionSummaryVM summaryVM = new TransactionSummaryVM();

        Mockito
                .when(transactionService.summary(initialDate, lastDate, monthType))
                .thenReturn(summaryVM);

        facade.summary(initialDate, lastDate, monthType);

        Mockito
                .verify(transactionService, Mockito.times(1))
                .summary(initialDate, lastDate, monthType);
    }
}