package com.greengoldfish.web.rest;

import com.greengoldfish.facade.UserFacade;
import com.greengoldfish.facade.dto.UserIdDTO;
import com.greengoldfish.facade.dto.UserToCreateDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private final UserFacade facade;

    public UserResource(UserFacade facade) {
        this.facade = facade;
    }

    @PostMapping("/users")
    public ResponseEntity<UserIdDTO> create(@Valid @RequestBody UserToCreateDTO dto) throws URISyntaxException {
        log.debug("REST request to create user");
        UserIdDTO response = facade.create(dto);
        return ResponseEntity.created(new URI("/api/user/" + response.getId())).body(response);
    }
}
