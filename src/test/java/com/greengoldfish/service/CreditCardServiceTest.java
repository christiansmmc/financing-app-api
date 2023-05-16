package com.greengoldfish.service;

import com.greengoldfish.domain.Authority;
import com.greengoldfish.domain.CreditCard;
import com.greengoldfish.domain.CreditCard.CreditCardBuilder;
import com.greengoldfish.domain.User;
import com.greengoldfish.domain.User.UserBuilder;
import com.greengoldfish.exception.BusinessException;
import com.greengoldfish.repository.CreditCardRepository;
import com.greengoldfish.service.impl.CreditCardServiceImpl;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

@ExtendWith(MockitoExtension.class)
class CreditCardServiceTest {

    private CreditCardService service;

    Faker faker = new Faker();

    @Mock
    private CreditCardRepository repository;
    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        service = new CreditCardServiceImpl(
                repository,
                userService
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
        User user = createUser(UnaryOperator.identity());
        CreditCard creditCard = createCreditCard(it -> it.user(user));

        Mockito.when(userService.findLoggedUser())
                .thenReturn(user);
        Mockito.when(repository.save(creditCard))
                .thenReturn(creditCard);

        CreditCard result = service.create(creditCard);
        Assertions.assertThat(result.getUser()).isNotNull();
        Assertions.assertThat(result.getIdentifier()).isEqualTo(creditCard.getIdentifier());
        Assertions.assertThat(result.getBestPurchaseDay()).isEqualTo(creditCard.getBestPurchaseDay());

        Mockito.verify(userService, Mockito.times(1))
                .findLoggedUser();
        Mockito.verify(repository, Mockito.times(1))
                .save(creditCard);
    }

    @Test
    void update() {
        User user = createUser(UnaryOperator.identity());
        CreditCard creditCard = createCreditCard(it -> {
            it.id(faker.number().randomNumber());
            it.user(user);
            return it;
        });

        Mockito.when(userService.findLoggedUser())
                .thenReturn(user);
        Mockito.when(repository.findById(creditCard.getId()))
                .thenReturn(Optional.of(creditCard));
        Mockito.when(repository.save(creditCard))
                .thenReturn(creditCard);

        service.update(creditCard);

        Mockito.verify(userService, Mockito.times(1))
                .findLoggedUser();
        Mockito.verify(repository, Mockito.times(1))
                .findById(creditCard.getId());
        Mockito.verify(repository, Mockito.times(1))
                .save(creditCard);
    }

    @Test
    void update_creditCardNotFromUser() {
        User user = createUser(UnaryOperator.identity());
        CreditCard creditCard = createCreditCard(it -> {
            it.id(faker.number().randomNumber());
            it.user(createUser(UnaryOperator.identity()));
            return it;
        });

        Mockito.when(userService.findLoggedUser())
                .thenReturn(user);
        Mockito.when(repository.findById(creditCard.getId()))
                .thenReturn(Optional.of(creditCard));

        org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class, () -> {
            service.update(creditCard);
        });

        Mockito.verify(userService, Mockito.times(1))
                .findLoggedUser();
        Mockito.verify(repository, Mockito.times(1))
                .findById(creditCard.getId());
        Mockito.verify(repository, Mockito.times(0))
                .save(creditCard);
    }

    @Test
    void delete() {
        User user = createUser(UnaryOperator.identity());
        CreditCard creditCard = createCreditCard(it -> {
            it.id(faker.number().randomNumber());
            it.user(user);
            return it;
        });

        Mockito.when(userService.findLoggedUser())
                .thenReturn(user);
        Mockito.when(repository.findById(creditCard.getId()))
                .thenReturn(Optional.of(creditCard));
        Mockito.doNothing()
                .when(repository).delete(creditCard);

        service.delete(creditCard.getId());

        Mockito.verify(userService, Mockito.times(1))
                .findLoggedUser();
        Mockito.verify(repository, Mockito.times(1))
                .findById(creditCard.getId());
        Mockito.verify(repository, Mockito.times(1))
                .delete(creditCard);
    }

    @Test
    void delete_creditCardNotFromUser() {
        User user = createUser(UnaryOperator.identity());
        CreditCard creditCard = createCreditCard(it -> {
            it.id(faker.number().randomNumber());
            it.user(createUser(UnaryOperator.identity()));
            return it;
        });

        Mockito.when(userService.findLoggedUser())
                .thenReturn(user);
        Mockito.when(repository.findById(creditCard.getId()))
                .thenReturn(Optional.of(creditCard));

        org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class, () -> {
            service.delete(creditCard.getId());
        });

        Mockito.verify(userService, Mockito.times(1))
                .findLoggedUser();
        Mockito.verify(repository, Mockito.times(1))
                .findById(creditCard.getId());
        Mockito.verify(repository, Mockito.times(0))
                .delete(creditCard);
    }

    @Test
    void findAllFromLoggedUser() {
        User user = createUser(UnaryOperator.identity());
        CreditCard creditCard = createCreditCard(it -> {
            it.user(user);
            return it;
        });

        Mockito.when(userService.findLoggedUser())
                .thenReturn(user);
        Mockito.when(repository.findAllByUser(user))
                .thenReturn(List.of(creditCard));

        service.findAllFromLoggedUser();

        Mockito.verify(userService, Mockito.times(1))
                .findLoggedUser();
        Mockito.verify(repository, Mockito.times(1))
                .findAllByUser(user);
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

    private CreditCard createCreditCard(UnaryOperator<CreditCard.CreditCardBuilder> builder) {
        CreditCardBuilder creditCardBuilder = CreditCard
                .builder()
                .identifier(faker.lorem().characters(10))
                .bestPurchaseDay(faker.random().nextInt(1, 31))
                .user(createUser(UnaryOperator.identity()));

        return builder.apply(creditCardBuilder).build();
    }
}