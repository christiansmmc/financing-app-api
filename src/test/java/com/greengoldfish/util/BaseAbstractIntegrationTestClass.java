package com.greengoldfish.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greengoldfish.GreenGoldfishApplication;
import com.greengoldfish.config.AuthoritiesConstants;
import com.greengoldfish.config.SecurityConfiguration;
import com.greengoldfish.domain.Authority;
import com.greengoldfish.domain.User;
import com.greengoldfish.facade.dto.AuthenticationRequestDTO;
import com.greengoldfish.repository.UserRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfiguration.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GreenGoldfishApplication.class)
@AutoConfigureMockMvc
public abstract class BaseAbstractIntegrationTestClass {

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected RepositoryCleanerService repositoryCleanerService;
    @Autowired
    protected UserRepository userRepository;

    Faker faker = new Faker();

    @AfterEach
    public void cleanDatabase() {
        repositoryCleanerService.resetDatabase();
    }

    public String getClientAccessToken(
            String username,
            String password
    ) throws Exception {
        AuthenticationRequestDTO dto = AuthenticationRequestDTO
                .builder()
                .email(username)
                .password(password)
                .build();

        String content = mockMvc.perform(
                        post("/api/authenticate")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtil.convertObjectToJsonBytes(dto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        var result = new ObjectMapper().readValue(content, HashMap.class);
        return "Bearer " + result.get("token");
    }

    public User getUserToLogin(
            String password
    ) {
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
