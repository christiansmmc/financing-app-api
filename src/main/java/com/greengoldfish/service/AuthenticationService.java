package com.greengoldfish.service;

import com.greengoldfish.facade.dto.AuthenticationRequestDTO;
import com.greengoldfish.facade.dto.AuthenticationResponseDTO;

public interface AuthenticationService {
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto);
}
