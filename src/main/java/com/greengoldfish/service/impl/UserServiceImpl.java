package com.greengoldfish.service.impl;

import com.greengoldfish.config.AuthoritiesConstants;
import com.greengoldfish.domain.Authority;
import com.greengoldfish.domain.User;
import com.greengoldfish.repository.AuthorityRepository;
import com.greengoldfish.repository.UserRepository;
import com.greengoldfish.service.UserService;
import com.greengoldfish.service.exceptions.BusinessException;
import com.greengoldfish.service.exceptions.enumerations.ErrorConstants;
import com.greengoldfish.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(User user) {
        throwIfLoginIsAlreadyRegistered(user.getEmail());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Authority authority = authorityRepository.findByName(AuthoritiesConstants.USER)
                .orElseThrow(BusinessException.notFound(Authority.class));
        Set<Authority> authorities = new HashSet<>();
        authorities.add(authority);

        user.setAuthorities(authorities);
        return repository.save(user);
    }

    @Override
    public User getLoggedUser() {
        return repository.findByEmail(SecurityUtils.getCurrentUserLogin().get())
                .orElseThrow(BusinessException.notFound(User.class));
    }

    private void throwIfLoginIsAlreadyRegistered(String login) {
        Optional<User> user = repository.findByEmail(login);
        BusinessException.throwIf(
                user.isPresent(),
                ErrorConstants.EMAIL_ALREADY_REGISTERED
        );
    }
}
