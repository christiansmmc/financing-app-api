package com.greengoldfish.repository;

import com.greengoldfish.domain.Authority;
import com.greengoldfish.domain.Transaction;
import com.greengoldfish.domain.User;
import net.datafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

    Faker faker = new Faker();

    private User user;

    @BeforeEach
    void setUp() {
        Authority userAuthority = authorityRepository.findByName("ROLE_USER")
                .orElseThrow();

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
    }

    @Test
    void findByEmail() {
        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());

        Assertions.assertThat(optionalUser).isPresent();
        Assertions.assertThat(optionalUser.get().getId()).isEqualTo(user.getId());
    }

    @Test
    void findByEmailFetchRoles() {
        Optional<User> optionalUser = userRepository.findByEmailFetchRoles(user.getEmail());

        Assertions.assertThat(optionalUser).isPresent();
        Assertions.assertThat(optionalUser.get().getId()).isEqualTo(user.getId());
    }
}