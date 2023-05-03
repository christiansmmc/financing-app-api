package com.greengoldfish.controller;

import com.greengoldfish.domain.Tag;
import com.greengoldfish.domain.Transaction;
import com.greengoldfish.domain.User;
import com.greengoldfish.domain.enumeration.TransactionType;
import com.greengoldfish.facade.dto.tag.TagIdDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToCreateDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToUpdateDTO;
import com.greengoldfish.repository.TagRepository;
import com.greengoldfish.repository.TransactionRepository;
import com.greengoldfish.util.BaseAbstractIntegrationTestClass;
import com.greengoldfish.util.TestUtil;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.function.UnaryOperator;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TransactionControllerIT extends BaseAbstractIntegrationTestClass {

    private static final String URL = "/api/transactions";
    private static final String URL_ID = "/api/transactions/{id}";

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    private String password = "TEST";
    private User user;

    Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        password = "TEST";
        user = getUserToLogin(password);
    }

    @Test
    public void create() throws Exception {
        Tag tag = tagRepository.findAll()
                .stream()
                .findAny()
                .orElseThrow();

        TransactionToCreateDTO toCreateDTO = TransactionToCreateDTO
                .builder()
                .name(faker.eldenRing().location())
                .description(faker.eldenRing().location())
                .amount(BigDecimal.TEN)
                .type(TransactionType.INCOME)
                .date(LocalDate.now())
                .tag(
                        TagIdDTO
                                .builder()
                                .id(tag.getId())
                                .build()
                )
                .build();

        mockMvc.perform(post(URL)
                        .header(
                                "Authorization", getClientAccessToken(
                                        user.getEmail(),
                                        password)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(toCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value(toCreateDTO.getName()))
                .andExpect(jsonPath("description").value(toCreateDTO.getDescription()))
                .andExpect(jsonPath("amount").value(toCreateDTO.getAmount()))
                .andExpect(jsonPath("type").value(toCreateDTO.getType().name()))
                .andExpect(jsonPath("date").value(toCreateDTO.getDate().toString()))
                .andExpect(jsonPath("tag.id").value(tag.getId().intValue()))
        ;
    }

    @Test
    public void create_invalidDto() throws Exception {
        TransactionToCreateDTO toCreateDTO = TransactionToCreateDTO
                .builder()
                .build();

        mockMvc.perform(post(URL)
                        .header(
                                "Authorization", getClientAccessToken(
                                        user.getEmail(),
                                        password)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(toCreateDTO)))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    public void findAllByLoggedUser() throws Exception {
        Transaction transaction = createTransaction(it -> it.user(user));

        mockMvc.perform(get(URL)
                        .header(
                                "Authorization", getClientAccessToken(
                                        user.getEmail(),
                                        password)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id").value(transaction.getId().intValue()))
                .andExpect(jsonPath("$[*].name").value(transaction.getName()))
                .andExpect(jsonPath("$[*].description").value(transaction.getDescription()))
                .andExpect(jsonPath("$[*].date").value(transaction.getDate().toString()))
                .andExpect(jsonPath("$[*].user.id").value(user.getId().intValue()))
        ;
    }

    @Test
    public void findAllByLoggedUser_byDate() throws Exception {
        int year = 2005;
        int month = 11;
        int day = 11;

        createTransaction(it -> it.user(user));
        createTransaction(it -> {
            it.user(user);
            it.date(LocalDate.of(year, month, day));
            return it;
        });

        String initialDateParam = String.format("initialDate=%d-%d-%d", year, month, day);
        String lastDateParam = String.format("lastDate=%d-%d-%d", year + 1, month, day);
        String url = String.format(URL + "?" + initialDateParam + "&" + lastDateParam);

        mockMvc.perform(get(url)
                        .header(
                                "Authorization", getClientAccessToken(
                                        user.getEmail(),
                                        password)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
        ;
    }

    @Test
    public void findById() throws Exception {
        Transaction transaction = createTransaction(it -> it.user(user));

        mockMvc.perform(get(URL_ID, transaction.getId())
                        .header(
                                "Authorization", getClientAccessToken(
                                        user.getEmail(),
                                        password)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transaction.getId().intValue()))
                .andExpect(jsonPath("$.name").value(transaction.getName()))
                .andExpect(jsonPath("$.description").value(transaction.getDescription()))
                .andExpect(jsonPath("$.date").value(transaction.getDate().toString()))
        ;
    }

    @Test
    public void update() throws Exception {
        Transaction transaction = createTransaction(it -> it.user(user));

        TransactionToUpdateDTO toUpdateDTO = TransactionToUpdateDTO
                .builder()
                .name(faker.friends().character())
                .description(faker.friends().character())
                .amount(BigDecimal.TEN)
                .type(TransactionType.OUTCOME)
                .date(LocalDate.now().withYear(1998))
                .build();

        mockMvc.perform(get(URL_ID, transaction.getId())
                        .header(
                                "Authorization", getClientAccessToken(
                                        user.getEmail(),
                                        password)
                        )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(toUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(transaction.getId().intValue()));
    }

    @Test
    public void deleteTransaction() throws Exception {
        Transaction transaction = createTransaction(it -> it.user(user));

        mockMvc.perform(delete(URL_ID, transaction.getId())
                        .header(
                                "Authorization", getClientAccessToken(
                                        user.getEmail(),
                                        password)
                        ))
                .andExpect(status().isNoContent());

        Assertions.assertThat(transactionRepository.findById(transaction.getId()))
                .isEmpty();
    }

    @Test
    public void summary() throws Exception {
        Transaction transaction = createTransaction(it -> {
            it.user(user);
            it.type(TransactionType.OUTCOME);
            return it;
        });

        mockMvc.perform(get(URL + "/summary?month=CURRENT")
                        .header(
                                "Authorization", getClientAccessToken(
                                        user.getEmail(),
                                        password)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalOutcome").value(transaction.getAmount().intValue()));
    }

    private Transaction createTransaction(UnaryOperator<Transaction.TransactionBuilder> builder) {
        Transaction.TransactionBuilder transactionBuilder = Transaction
                .builder()
                .id(faker.number().randomNumber())
                .name(faker.eldenRing().npc())
                .amount(BigDecimal.TEN)
                .type(TransactionType.INCOME)
                .date(LocalDate.now());

        Transaction transaction = builder.apply(transactionBuilder).build();
        return transactionRepository.saveAndFlush(transaction);
    }
}
