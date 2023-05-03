package com.greengoldfish.controller;

import com.greengoldfish.domain.User;
import com.greengoldfish.facade.dto.user.UserToCreateDTO;
import com.greengoldfish.facade.dto.user.UserToUpdateDTO;
import com.greengoldfish.repository.UserRepository;
import com.greengoldfish.util.BaseAbstractIntegrationTestClass;
import com.greengoldfish.util.TestUtil;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerIT extends BaseAbstractIntegrationTestClass {

    private static final String URL = "/api/users";

    @Autowired
    private UserRepository userRepository;
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
        UserToCreateDTO toCreateDTO = UserToCreateDTO
                .builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(toCreateDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").hasJsonPath())
        ;
    }

    @Test
    public void create_invalidDto() throws Exception {
        UserToCreateDTO toCreateDTO = UserToCreateDTO
                .builder()
                .build();

        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(toCreateDTO)))
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    public void update() throws Exception {
        UserToUpdateDTO toUpdateDTO = UserToUpdateDTO
                .builder()
                .email(faker.internet().emailAddress())
                .password(faker.internet().password())
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .build();

        mockMvc.perform(patch(URL)
                        .header("Authorization", getClientAccessToken(user.getEmail(), password))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtil.convertObjectToJsonBytes(toUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(header().exists("X-New-Token"));

        User userUpdated = userRepository.findById(user.getId())
                .orElseThrow();

        Assertions.assertThat(userUpdated.getEmail()).isEqualTo(toUpdateDTO.getEmail());
        Assertions.assertThat(userUpdated.getFirstName()).isEqualTo(toUpdateDTO.getFirstName());
        Assertions.assertThat(userUpdated.getLastName()).isEqualTo(toUpdateDTO.getLastName());
    }

    @Test
    public void getInfoByLoggedUser() throws Exception {
        mockMvc.perform(get(URL)
                        .header("Authorization", getClientAccessToken(user.getEmail(), password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }
}
