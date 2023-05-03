package com.greengoldfish.repository;

import com.greengoldfish.domain.Authority;
import com.greengoldfish.domain.Transaction;
import com.greengoldfish.domain.User;
import com.greengoldfish.domain.enumeration.TransactionType;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    Faker faker = new Faker();

    private User user;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        Authority userAuthority = authorityRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    return authorityRepository.saveAndFlush(Authority.builder().name("ROLE_USER").build());
                });
        user = userRepository.saveAndFlush(
                User
                        .builder()
                        .email(faker.internet().emailAddress())
                        .password(faker.internet().password(60, 60))
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .authorities(new HashSet<>(List.of(userAuthority)))
                        .build()
        );
        transaction = transactionRepository.saveAndFlush(
                Transaction
                        .builder()
                        .name(faker.eldenRing().npc())
                        .amount(BigDecimal.TEN)
                        .type(TransactionType.INCOME)
                        .date(LocalDate.now())
                        .user(user)
                        .build()
        );
    }

    @Test
    void findAllByUser() {
        List<Transaction> result = repository.findAllByUser(user);

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0)).isEqualTo(transaction);
    }

    @Test
    void findAllByUserAndDateIsBetween() {
        List<Transaction> result = repository.findAllByUserAndDateIsBetween(
                user,
                LocalDate.now().minusMonths(1),
                LocalDate.now()
        );

        Assertions.assertThat(result.size()).isEqualTo(1);
        Assertions.assertThat(result.get(0)).isEqualTo(transaction);
    }

    @Test
    void findByIdAndUser() {
        Optional<Transaction> result = repository.findByIdAndUser(
                transaction.getId(),
                user
        );

        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result.get()).isEqualTo(transaction);
    }
}