package com.greengoldfish.service.impl;

import com.greengoldfish.config.JwtService;
import com.greengoldfish.domain.User;
import com.greengoldfish.facade.dto.AuthenticationRequestDTO;
import com.greengoldfish.facade.dto.AuthenticationResponseDTO;
import com.greengoldfish.repository.UserRepository;
import com.greengoldfish.service.AuthenticationService;
import com.greengoldfish.service.exceptions.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Override
    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword()
                )
        );

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(BusinessException.of("User not found"));
        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponseDTO
                .builder()
                .token(jwtToken)
                .build();
    }
}
