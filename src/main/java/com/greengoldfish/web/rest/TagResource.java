package com.greengoldfish.web.rest;

import com.greengoldfish.config.AuthoritiesConstants;
import com.greengoldfish.facade.TagFacade;
import com.greengoldfish.facade.dto.tag.TagDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TagResource {

    private final TagFacade facade;

    @Secured(AuthoritiesConstants.USER)
    @GetMapping("/tags")
    public ResponseEntity<List<TagDTO>> findAll() {
        log.debug("REST request to get all tag");
        List<TagDTO> response = facade.findAll();
        return ResponseEntity.ok().body(response);
    }
}
