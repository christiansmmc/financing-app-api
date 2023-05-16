package com.greengoldfish.facade;

import com.greengoldfish.domain.CreditCard;
import com.greengoldfish.facade.dto.creditcard.CreditCardIdDTO;
import com.greengoldfish.facade.dto.creditcard.CreditCardSimpleDTO;
import com.greengoldfish.facade.dto.creditcard.CreditCardToCreateDTO;
import com.greengoldfish.facade.dto.creditcard.CreditCardToUpdateDTO;
import com.greengoldfish.facade.mapper.CreditCardMapper;
import com.greengoldfish.service.CreditCardService;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class CreditCardFacadeTest {

    private CreditCardFacade facade;

    Faker faker = new Faker();

    @Mock
    private CreditCardMapper mapper;
    @Mock
    private CreditCardService service;

    @BeforeEach
    void setUp() {
        facade = new CreditCardFacade(
                mapper,
                service
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
        CreditCardToCreateDTO toCreateDto = new CreditCardToCreateDTO();
        CreditCard creditCard = new CreditCard();
        CreditCardIdDTO idDto = new CreditCardIdDTO();

        Mockito.when(mapper.toEntity(toCreateDto))
                .thenReturn(creditCard);
        Mockito.when(service.create(creditCard))
                .thenReturn(creditCard);
        Mockito.when(mapper.toIdDto(creditCard))
                .thenReturn(idDto);

        facade.create(toCreateDto);

        Mockito.verify(mapper, Mockito.times(1))
                .toEntity(toCreateDto);
        Mockito.verify(service, Mockito.times(1))
                .create(creditCard);
        Mockito.verify(mapper, Mockito.times(1))
                .toIdDto(creditCard);
    }

    @Test
    void update() {
        CreditCardToUpdateDTO toUpdateDto = new CreditCardToUpdateDTO();
        CreditCard creditCard = new CreditCard();
        CreditCardIdDTO idDto = new CreditCardIdDTO();

        Mockito.when(mapper.toEntity(toUpdateDto))
                .thenReturn(creditCard);
        Mockito.when(service.update(creditCard))
                .thenReturn(creditCard);
        Mockito.when(mapper.toIdDto(creditCard))
                .thenReturn(idDto);

        facade.update(toUpdateDto);

        Mockito.verify(mapper, Mockito.times(1))
                .toEntity(toUpdateDto);
        Mockito.verify(service, Mockito.times(1))
                .update(creditCard);
        Mockito.verify(mapper, Mockito.times(1))
                .toIdDto(creditCard);
    }

    @Test
    void delete() {
        Long id = faker.number().randomNumber();

        Mockito.doNothing()
                .when(service).delete(id);

        facade.delete(id);

        Mockito.verify(service, Mockito.times(1))
                .delete(id);
    }

    @Test
    void findAllByLoggedUser() {
        CreditCard creditCard = new CreditCard();
        List<CreditCard> creditCardList = List.of(creditCard);

        CreditCardSimpleDTO simpleDto = new CreditCardSimpleDTO();

        Mockito.when(service.findAllFromLoggedUser())
                .thenReturn(creditCardList);
        Mockito.when(mapper.toSimpleDto(creditCard))
                .thenReturn(simpleDto);

        facade.findAllByLoggedUser();

        Mockito.verify(service, Mockito.times(1))
                .findAllFromLoggedUser();
        Mockito.verify(mapper, Mockito.times(1))
                .toSimpleDto(creditCard);
    }
}