package com.greengoldfish.web.rest;

import com.greengoldfish.facade.dto.AuthenticationRequestDTO;
import com.greengoldfish.facade.dto.AuthenticationResponseDTO;
import com.greengoldfish.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class AuthenticationResource {

    private final AuthenticationService service;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(
            @Valid @RequestBody AuthenticationRequestDTO dto
    ) {
        log.debug("REST request to login user");
        AuthenticationResponseDTO response = service.authenticate(dto);
        return ResponseEntity.ok().body(response);
    }
}
