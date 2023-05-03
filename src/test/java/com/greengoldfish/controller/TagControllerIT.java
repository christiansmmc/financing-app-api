package com.greengoldfish.controller;

import com.greengoldfish.domain.User;
import com.greengoldfish.repository.TagRepository;
import com.greengoldfish.util.BaseAbstractIntegrationTestClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TagControllerIT extends BaseAbstractIntegrationTestClass {

    private static final String URL = "/api/tags";

    @Autowired
    private TagRepository tagRepository;

    private String password = "TEST";
    private User user;

    @BeforeEach
    void setUp() {
        password = "TEST";
        user = getUserToLogin(password);
    }

    @Test
    public void findAll() throws Exception {
        long tagSize = tagRepository.count();

        mockMvc.perform(get(URL)
                        .header(
                                "Authorization", getClientAccessToken(
                                        user.getEmail(),
                                        password)
                        ))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.size()").value(tagSize))
        ;
    }
}
