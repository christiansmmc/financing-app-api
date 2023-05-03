package com.greengoldfish.controller;

import com.greengoldfish.config.AuthoritiesConstants;
import com.greengoldfish.facade.UserFacade;
import com.greengoldfish.facade.dto.user.UserIdDTO;
import com.greengoldfish.facade.dto.user.UserToCreateDTO;
import com.greengoldfish.facade.dto.user.UserToGetDTO;
import com.greengoldfish.facade.dto.user.UserToUpdateDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class UserController {

    private final UserFacade facade;

    @PostMapping("/users")
    public ResponseEntity<UserIdDTO> create(
            @Valid @RequestBody UserToCreateDTO dto
    ) throws URISyntaxException {
        log.debug("REST request to create user");
        UserIdDTO response = facade.create(dto);
        return ResponseEntity.created(new URI("/api/users/" + response.getId())).body(response);
    }

    @Secured(AuthoritiesConstants.USER)
    @PatchMapping("/users")
    public ResponseEntity<UserIdDTO> update(@Valid @RequestBody UserToUpdateDTO dto) {
        log.debug("REST request to update user");
        UserIdDTO response = facade.update(dto);

        String jwt = (String) RequestContextHolder.currentRequestAttributes().getAttribute("jwt", RequestAttributes.SCOPE_REQUEST);
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-New-Token", jwt);

        return ResponseEntity.ok().headers(headers).body(response);
    }

    @Secured(AuthoritiesConstants.USER)
    @GetMapping("/users")
    public ResponseEntity<UserToGetDTO> getInfoByLoggedUser() {
        log.debug("REST request to get logged user info");
        UserToGetDTO response = facade.getInfoByLoggedUser();
        return ResponseEntity.ok().body(response);
    }
}
