package com.greengoldfish.controller;

import com.greengoldfish.config.AuthoritiesConstants;
import com.greengoldfish.domain.Authority;
import com.greengoldfish.domain.CreditCard;
import com.greengoldfish.domain.CreditCard.CreditCardBuilder;
import com.greengoldfish.domain.User;
import com.greengoldfish.facade.dto.creditcard.CreditCardToCreateDTO;
import com.greengoldfish.facade.dto.creditcard.CreditCardToUpdateDTO;
import com.greengoldfish.repository.CreditCardRepository;
import com.greengoldfish.util.BaseAbstractIntegrationTestClass;
import com.greengoldfish.util.TestUtil;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.HashSet;
import java.util.List;
import java.util.function.UnaryOperator;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreditCardControllerIT extends BaseAbstractIntegrationTestClass {

    private static final String URL = "/api/credit-cards";
    private static final String URL_ID = "/api/credit-cards/{id}";

    @Autowired
    private CreditCardRepository creditCardRepository;

    Faker faker = new Faker();

    private final String password = faker.internet().password();
    private User user;

    @BeforeEach
    void setUp() {
        user = getUserToLogin(password);
    }

    @Test
    public void create() throws Exception {
        CreditCardToCreateDTO dto = CreditCardToCreateDTO
                .builder()
                .identifier(faker.lorem().characters(15))
                .bestPurchaseDay((int) faker.number().randomNumber(1, true))
                .build();

        mockMvc.perform(post(URL)
                        .header("Authorization", getClientAccessToken(user.getEmail(), password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").hasJsonPath());
    }

    @Test
    public void create_invalidDto() throws Exception {
        CreditCardToCreateDTO dto = CreditCardToCreateDTO
                .builder()
                .build();

        mockMvc.perform(post(URL)
                        .header("Authorization", getClientAccessToken(user.getEmail(), password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void update() throws Exception {
        CreditCard creditCard = createCreditCard(UnaryOperator.identity());

        CreditCardToUpdateDTO dto = CreditCardToUpdateDTO
                .builder()
                .id(creditCard.getId())
                .identifier(faker.lorem().characters(10))
                .bestPurchaseDay(faker.random().nextInt(1, 31))
                .build();

        mockMvc.perform(patch(URL_ID, creditCard.getId())
                        .header("Authorization", getClientAccessToken(user.getEmail(), password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").hasJsonPath())
                .andExpect(jsonPath("id").value(creditCard.getId()));
    }

    @Test
    public void update_invalidDto() throws Exception {
        CreditCard creditCard = createCreditCard(UnaryOperator.identity());

        CreditCardToUpdateDTO dto = CreditCardToUpdateDTO
                .builder()
                .build();

        mockMvc.perform(patch(URL_ID, creditCard.getId())
                        .header("Authorization", getClientAccessToken(user.getEmail(), password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void update_idNotFound() throws Exception {
        CreditCard creditCard = createCreditCard(UnaryOperator.identity());

        Long fakeId = creditCard.getId() + 1;

        CreditCardToUpdateDTO dto = CreditCardToUpdateDTO
                .builder()
                .id(fakeId)
                .identifier(faker.lorem().characters(10))
                .bestPurchaseDay(faker.random().nextInt(1, 31))
                .build();

        mockMvc.perform(patch(URL_ID, fakeId)
                        .header("Authorization", getClientAccessToken(user.getEmail(), password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void update_creditCardNotFromUser() throws Exception {
        CreditCard creditCard = createCreditCard(it -> it.user(createUser()));

        CreditCardToUpdateDTO dto = CreditCardToUpdateDTO
                .builder()
                .id(creditCard.getId())
                .identifier(faker.lorem().characters(10))
                .bestPurchaseDay(faker.random().nextInt(1, 31))
                .build();

        mockMvc.perform(patch(URL_ID, creditCard.getId())
                        .header("Authorization", getClientAccessToken(user.getEmail(), password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void delete_creditCard() throws Exception {
        CreditCard creditCard = createCreditCard(it -> it.user(user));

        mockMvc.perform(delete(URL_ID, creditCard.getId())
                        .header("Authorization", getClientAccessToken(user.getEmail(), password)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void delete_creditCardNotFromUser() throws Exception {
        CreditCard creditCard = createCreditCard(it -> it.user(createUser()));

        mockMvc.perform(delete(URL_ID, creditCard.getId())
                        .header("Authorization", getClientAccessToken(user.getEmail(), password)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void findAllByLoggedUser() throws Exception {
        createCreditCard(it -> it.user(user));

        mockMvc.perform(get(URL)
                        .header("Authorization", getClientAccessToken(user.getEmail(), password)))
                .andExpect(status().isOk());
    }

    private CreditCard createCreditCard(UnaryOperator<CreditCard.CreditCardBuilder> builder) {
        CreditCardBuilder creditCardBuilder = CreditCard
                .builder()
                .identifier(faker.lorem().characters(10))
                .bestPurchaseDay(faker.random().nextInt(1, 31))
                .user(user);

        CreditCard creditCard = builder.apply(creditCardBuilder).build();
        return creditCardRepository.saveAndFlush(creditCard);
    }

    private User createUser() {
        User user = User
                .builder()
                .email(faker.internet().emailAddress())
                .password(passwordEncoder.encode(password))
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .authorities(new HashSet<>(List.of(new Authority(AuthoritiesConstants.USER))))
                .build();

        return userRepository.saveAndFlush(user);
    }
}
