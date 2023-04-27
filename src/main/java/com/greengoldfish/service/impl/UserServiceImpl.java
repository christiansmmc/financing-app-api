package com.greengoldfish.service.impl;

import com.greengoldfish.config.AuthoritiesConstants;
import com.greengoldfish.config.JwtService;
import com.greengoldfish.config.UserPrincipal;
import com.greengoldfish.domain.Authority;
import com.greengoldfish.domain.User;
import com.greengoldfish.repository.AuthorityRepository;
import com.greengoldfish.repository.UserRepository;
import com.greengoldfish.service.UserService;
import com.greengoldfish.service.exceptions.BusinessException;
import com.greengoldfish.service.exceptions.enumerations.ErrorConstants;
import com.greengoldfish.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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

    @Override
    public User update(User user) {
        User userToUpdate = getLoggedUser();

        if (!Objects.equals(user.getEmail(), userToUpdate.getEmail())) {
            throwIfLoginIsAlreadyRegistered(user.getEmail());
        }

        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setLastName(user.getLastName());
        userToUpdate.setEmail(user.getEmail());

        if (user.getPassword() != null) {
            userToUpdate.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        String jwt = createNewTokenForLoggedUser(user);
        RequestContextHolder.currentRequestAttributes().setAttribute("jwt", jwt, RequestAttributes.SCOPE_REQUEST);

        return repository.save(userToUpdate);
    }

    @Override
    public User getByLoggedUser() {
        return getLoggedUser();
    }

    public String createNewTokenForLoggedUser(User user) {
        var grantedAuthorities = user
                .getAuthorities()
                .stream()
                .map(it -> new SimpleGrantedAuthority(it.getName()))
                .collect(Collectors.toSet());

        var applicationAuthenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), grantedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(applicationAuthenticationToken);
        return jwtService.generateToken(UserPrincipal.create(user));
    }

    private void throwIfLoginIsAlreadyRegistered(String login) {
        Optional<User> user = repository.findByEmail(login);
        BusinessException.throwIf(
                user.isPresent(),
                ErrorConstants.EMAIL_ALREADY_REGISTERED
        );
    }
}
