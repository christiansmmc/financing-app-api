package com.greengoldfish.service;

import com.greengoldfish.config.AuthoritiesConstants;
import com.greengoldfish.config.JwtService;
import com.greengoldfish.domain.Authority;
import com.greengoldfish.domain.User;
import com.greengoldfish.repository.AuthorityRepository;
import com.greengoldfish.repository.UserRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private AuthorityRepository authorityRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(
                userRepository,
                authorityRepository,
                passwordEncoder,
                jwtService
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();

        User user = User.builder()
                .email(email)
                .password(password)
                .build();

        Authority authority = Authority
                .builder()
                .name(AuthoritiesConstants.USER)
                .build();

        Mockito.when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(password))
                .thenReturn(faker.lorem().characters(60));
        Mockito.when(authorityRepository.findByName(AuthoritiesConstants.USER))
                .thenReturn(Optional.of(authority));
        Mockito.when(userRepository.save(user))
                .thenReturn(user);

        userService.create(user);

        Mockito.verify(userRepository)
                .findByEmail(email);
        Mockito.verify(passwordEncoder)
                .encode(password);
        Mockito.verify(authorityRepository)
                .findByName(AuthoritiesConstants.USER);
        Mockito.verify(userRepository)
                .save(user);
    }
}