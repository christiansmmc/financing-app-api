package com.greengoldfish.service;

import com.greengoldfish.domain.Authority;
import com.greengoldfish.domain.Tag;
import com.greengoldfish.domain.Tag.TagBuilder;
import com.greengoldfish.domain.Transaction;
import com.greengoldfish.domain.Transaction.TransactionBuilder;
import com.greengoldfish.domain.User;
import com.greengoldfish.domain.User.UserBuilder;
import com.greengoldfish.domain.enumeration.MonthType;
import com.greengoldfish.domain.enumeration.TransactionType;
import com.greengoldfish.exception.BusinessException;
import com.greengoldfish.repository.TransactionRepository;
import com.greengoldfish.web.rest.vm.TransactionSummaryVM;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    private TransactionService transactionService;

    Faker faker = new Faker();

    @Mock
    private TransactionRepository repository;
    @Mock
    private UserService userService;
    @Mock
    private TagService tagService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionServiceImpl(
                repository,
                userService,
                tagService
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
        User user = createUser(UnaryOperator.identity());
        Tag tag = createTag(UnaryOperator.identity());
        Transaction transactionToCreate = createTransaction(it -> {
            it.tag(tag);
            it.date(null);
            return it;
        });

        Mockito
                .when(userService.getLoggedUser())
                .thenReturn(user);
        Mockito
                .when(tagService.findById(tag.getId()))
                .thenReturn(tag);
        Mockito
                .when(repository.save(transactionToCreate))
                .thenReturn(transactionToCreate);

        Transaction result = transactionService.create(transactionToCreate);
        Assertions.assertThat(result.getDate()).isNotNull();
        Assertions.assertThat(result.getUser()).isEqualTo(user);
        Assertions.assertThat(result.getTag()).isEqualTo(transactionToCreate.getTag());
        Assertions.assertThat(result.getName()).isEqualTo(transactionToCreate.getName());
        Assertions.assertThat(result.getAmount()).isEqualTo(transactionToCreate.getAmount());

        Mockito
                .verify(userService, Mockito.times(1))
                .getLoggedUser();
        Mockito
                .verify(tagService, Mockito.times(1))
                .findById(tag.getId());
        Mockito
                .verify(repository, Mockito.times(1))
                .save(transactionToCreate);
    }

    @Test
    void create_withDate() {
        User user = createUser(UnaryOperator.identity());
        Tag tag = createTag(UnaryOperator.identity());
        Transaction transactionToCreate = createTransaction(it -> {
            it.tag(tag);
            it.date(LocalDate.now().minusMonths(1));
            return it;
        });

        Mockito
                .when(userService.getLoggedUser())
                .thenReturn(user);
        Mockito
                .when(tagService.findById(tag.getId()))
                .thenReturn(tag);
        Mockito
                .when(repository.save(transactionToCreate))
                .thenReturn(transactionToCreate);

        Transaction result = transactionService.create(transactionToCreate);
        Assertions.assertThat(result.getUser()).isEqualTo(user);
        Assertions.assertThat(result.getDate()).isEqualTo(transactionToCreate.getDate());
        Assertions.assertThat(result.getTag()).isEqualTo(transactionToCreate.getTag());
        Assertions.assertThat(result.getName()).isEqualTo(transactionToCreate.getName());
        Assertions.assertThat(result.getAmount()).isEqualTo(transactionToCreate.getAmount());

        Mockito
                .verify(userService, Mockito.times(1))
                .getLoggedUser();
        Mockito
                .verify(tagService, Mockito.times(1))
                .findById(tag.getId());
        Mockito
                .verify(repository, Mockito.times(1))
                .save(transactionToCreate);
    }

    @Test
    void findAll_withoutDate() {
        User user = createUser(UnaryOperator.identity());
        Transaction transactionCreated = createTransaction(UnaryOperator.identity());

        Mockito
                .when(userService.getLoggedUser())
                .thenReturn(user);
        Mockito
                .when(repository.findAllByUser(user))
                .thenReturn(List.of(transactionCreated));

        List<Transaction> result = transactionService.findAllByLoggedUser(null, null);

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(transactionCreated.getId());

        Mockito
                .verify(userService, Mockito.times(1))
                .getLoggedUser();
        Mockito
                .verify(repository, Mockito.times(1))
                .findAllByUser(user);
        Mockito
                .verify(repository, Mockito.times(0))
                .findAllByUserAndDateIsBetween(Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    void findAll_withDate() {
        LocalDate initialDate = LocalDate.now().minusMonths(1);
        LocalDate lastDate = LocalDate.now();
        User user = createUser(UnaryOperator.identity());
        Transaction transactionCreated = createTransaction(UnaryOperator.identity());

        Mockito
                .when(userService.getLoggedUser())
                .thenReturn(user);
        Mockito
                .when(repository.findAllByUserAndDateIsBetween(user, initialDate, lastDate))
                .thenReturn(List.of(transactionCreated));

        List<Transaction> result = transactionService.findAllByLoggedUser(initialDate, lastDate);

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0).getId()).isEqualTo(transactionCreated.getId());

        Mockito
                .verify(userService, Mockito.times(1))
                .getLoggedUser();
        Mockito
                .verify(repository, Mockito.times(1))
                .findAllByUserAndDateIsBetween(user, initialDate, lastDate);
        Mockito
                .verify(repository, Mockito.times(0))
                .findAllByUser(Mockito.any());
    }

    @Test
    void findByIdAndLoggedUser() {
        User user = createUser(UnaryOperator.identity());
        Transaction transactionCreated = createTransaction(UnaryOperator.identity());

        Mockito
                .when(userService.getLoggedUser())
                .thenReturn(user);
        Mockito
                .when(repository.findByIdAndUser(transactionCreated.getId(), user))
                .thenReturn(Optional.of(transactionCreated));

        transactionService.findByIdAndLoggedUser(transactionCreated.getId());

        Mockito
                .verify(userService, Mockito.times(1))
                .getLoggedUser();
        Mockito
                .verify(repository, Mockito.times(1))
                .findByIdAndUser(transactionCreated.getId(), user);
    }

    @Test
    void findByIdAndLoggedUser_notFound() {
        User user = createUser(UnaryOperator.identity());
        Long fakeTransactionId = faker.number().randomNumber();

        Mockito
                .when(userService.getLoggedUser())
                .thenReturn(user);
        Mockito
                .when(repository.findByIdAndUser(fakeTransactionId, user))
                .thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class, () -> {
            transactionService.findByIdAndLoggedUser(fakeTransactionId);
        });

        Mockito
                .verify(userService, Mockito.times(1))
                .getLoggedUser();
        Mockito
                .verify(repository, Mockito.times(1))
                .findByIdAndUser(fakeTransactionId, user);
    }

    @Test
    void update() {
        Long transactionId = faker.number().randomNumber();
        User user = createUser(UnaryOperator.identity());
        Tag tag = createTag(UnaryOperator.identity());
        Transaction transaction = createTransaction(it -> {
            it.id(transactionId);
            it.tag(tag);
            return it;
        });
        Transaction transactionToUpdate = createTransaction(it -> {
            it.id(transactionId);
            it.user(user);
            it.tag(null);
            return it;
        });

        Mockito
                .when(userService.getLoggedUser())
                .thenReturn(user);
        Mockito
                .when(repository.findByIdAndUser(transaction.getId(), user))
                .thenReturn(Optional.of(transactionToUpdate));
        Mockito
                .when(tagService.findById(tag.getId()))
                .thenReturn(tag);
        Mockito
                .when(repository.save(transactionToUpdate))
                .thenReturn(transactionToUpdate);

        Transaction result = transactionService.update(transaction);
        Assertions.assertThat(result.getDate()).isNotNull();
        Assertions.assertThat(result.getUser()).isEqualTo(user);
        Assertions.assertThat(result.getTag()).isEqualTo(transactionToUpdate.getTag());
        Assertions.assertThat(result.getName()).isEqualTo(transactionToUpdate.getName());
        Assertions.assertThat(result.getAmount()).isEqualTo(transactionToUpdate.getAmount());

        Mockito
                .verify(userService, Mockito.times(1))
                .getLoggedUser();
        Mockito
                .verify(repository, Mockito.times(1))
                .findByIdAndUser(transaction.getId(), user);
        Mockito
                .verify(tagService, Mockito.times(1))
                .findById(tag.getId());
        Mockito
                .verify(repository, Mockito.times(1))
                .save(transactionToUpdate);
    }

    @Test
    void update_withoutTag() {
        Long transactionId = faker.number().randomNumber();
        User user = createUser(UnaryOperator.identity());
        Transaction transaction = createTransaction(it -> {
            it.id(transactionId);
            it.tag(null);
            return it;
        });
        Transaction transactionToUpdate = createTransaction(it -> {
            it.id(transactionId);
            it.user(user);
            it.tag(null);
            return it;
        });

        Mockito
                .when(userService.getLoggedUser())
                .thenReturn(user);
        Mockito
                .when(repository.findByIdAndUser(transaction.getId(), user))
                .thenReturn(Optional.of(transactionToUpdate));
        Mockito
                .when(repository.save(transactionToUpdate))
                .thenReturn(transactionToUpdate);

        Transaction result = transactionService.update(transaction);
        Assertions.assertThat(result.getTag()).isNull();
        Assertions.assertThat(result.getDate()).isNotNull();
        Assertions.assertThat(result.getUser()).isEqualTo(user);
        Assertions.assertThat(result.getName()).isEqualTo(transactionToUpdate.getName());
        Assertions.assertThat(result.getAmount()).isEqualTo(transactionToUpdate.getAmount());

        Mockito
                .verify(userService, Mockito.times(1))
                .getLoggedUser();
        Mockito
                .verify(repository, Mockito.times(1))
                .findByIdAndUser(transaction.getId(), user);
        Mockito
                .verify(tagService, Mockito.times(0))
                .findById(Mockito.any());
        Mockito
                .verify(repository, Mockito.times(1))
                .save(transactionToUpdate);
    }

    @Test
    void delete() {
        User user = createUser(UnaryOperator.identity());
        Transaction transaction = createTransaction(UnaryOperator.identity());

        Mockito
                .when(userService.getLoggedUser())
                .thenReturn(user);
        Mockito
                .when(repository.findByIdAndUser(transaction.getId(), user))
                .thenReturn(Optional.of(transaction));
        Mockito
                .doNothing()
                .when(repository).delete(transaction);

        transactionService.delete(transaction.getId());

        Mockito
                .verify(userService, Mockito.times(1))
                .getLoggedUser();
        Mockito
                .verify(repository, Mockito.times(1))
                .findByIdAndUser(transaction.getId(), user);
        Mockito
                .verify(repository, Mockito.times(1))
                .delete(transaction);
    }

    @Test
    void summary_currentMonth() {
        User user = createUser(UnaryOperator.identity());
        Transaction transactionIncome = createTransaction(it -> {
            it.type(TransactionType.INCOME);
            it.amount(BigDecimal.valueOf(1000));
            return it;
        });
        Transaction transactionOutcome = createTransaction(it -> {
            it.type(TransactionType.OUTCOME);
            it.amount(BigDecimal.valueOf(500));
            return it;
        });

        Mockito
                .when(userService.getLoggedUser())
                .thenReturn(user);
        Mockito
                .when(repository.findAllByUserAndDateIsBetween(Mockito.eq(user), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
                .thenReturn(List.of(transactionIncome, transactionOutcome));

        TransactionSummaryVM result = transactionService.summary(null, null, MonthType.CURRENT);

        Assertions.assertThat(result.getInitialDate().getMonth())
                .isEqualTo(LocalDate.now().getMonth());
        Assertions.assertThat(result.getLastDate().getMonth())
                .isEqualTo(LocalDate.now().getMonth());
        Assertions.assertThat(result.getTotalIncome())
                .isEqualTo(transactionIncome.getAmount());
        Assertions.assertThat(result.getTotalOutcome())
                .isEqualTo(transactionOutcome.getAmount());

        Mockito
                .verify(userService, Mockito.times(1))
                .getLoggedUser();
        Mockito
                .verify(repository, Mockito.times(1))
                .findAllByUserAndDateIsBetween(Mockito.eq(user), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class));
    }

    @Test
    void summary_lastMonth() {
        User user = createUser(UnaryOperator.identity());
        Transaction transactionIncome = createTransaction(it -> {
            it.type(TransactionType.INCOME);
            it.amount(BigDecimal.valueOf(1000));
            return it;
        });
        Transaction transactionOutcome = createTransaction(it -> {
            it.type(TransactionType.OUTCOME);
            it.amount(BigDecimal.valueOf(500));
            return it;
        });

        Mockito
                .when(userService.getLoggedUser())
                .thenReturn(user);
        Mockito
                .when(repository.findAllByUserAndDateIsBetween(Mockito.eq(user), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class)))
                .thenReturn(List.of(transactionIncome, transactionOutcome));

        TransactionSummaryVM result = transactionService.summary(null, null, MonthType.LAST);

        Assertions.assertThat(result.getInitialDate().getMonth())
                .isEqualTo(LocalDate.now().minusMonths(1).getMonth());
        Assertions.assertThat(result.getLastDate().getMonth())
                .isEqualTo(LocalDate.now().minusMonths(1).getMonth());
        Assertions.assertThat(result.getTotalIncome())
                .isEqualTo(transactionIncome.getAmount());
        Assertions.assertThat(result.getTotalOutcome())
                .isEqualTo(transactionOutcome.getAmount());

        Mockito
                .verify(userService, Mockito.times(1))
                .getLoggedUser();
        Mockito
                .verify(repository, Mockito.times(1))
                .findAllByUserAndDateIsBetween(Mockito.eq(user), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class));
    }

    @Test
    void summary_noDateException() {
        User user = createUser(UnaryOperator.identity());

        Mockito
                .when(userService.getLoggedUser())
                .thenReturn(user);

        org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class, () -> {
            transactionService.summary(null, null, null);
        });

        Mockito
                .verify(userService, Mockito.times(1))
                .getLoggedUser();
        Mockito
                .verify(repository, Mockito.times(0))
                .findAllByUserAndDateIsBetween(Mockito.any(User.class), Mockito.any(LocalDate.class), Mockito.any(LocalDate.class));
    }

    private User createUser(UnaryOperator<UserBuilder> builder) {
        UserBuilder userBuilder = User
                .builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password(60, 60))
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .authorities(new HashSet<>(List.of(new Authority())));

        return builder.apply(userBuilder).build();
    }

    private Tag createTag(UnaryOperator<TagBuilder> builder) {
        TagBuilder tagBuilder = Tag
                .builder()
                .id(faker.number().randomNumber())
                .name(faker.eldenRing().npc());

        return builder.apply(tagBuilder).build();
    }

    private Transaction createTransaction(UnaryOperator<TransactionBuilder> builder) {
        TransactionBuilder transactionBuilder = Transaction
                .builder()
                .id(faker.number().randomNumber())
                .name(faker.eldenRing().npc())
                .amount(BigDecimal.TEN)
                .type(TransactionType.INCOME)
                .date(LocalDate.now());

        return builder.apply(transactionBuilder).build();
    }
}