package com.greengoldfish.web.rest;

import com.greengoldfish.config.AuthoritiesConstants;
import com.greengoldfish.facade.TagFacade;
import com.greengoldfish.facade.UserFacade;
import com.greengoldfish.facade.dto.tag.TagDTO;
import com.greengoldfish.facade.dto.user.UserIdDTO;
import com.greengoldfish.facade.dto.user.UserToCreateDTO;
import com.greengoldfish.facade.dto.user.UserToGetDTO;
import com.greengoldfish.facade.dto.user.UserToUpdateDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TagResource {

    private final TagFacade facade;

    @Secured(AuthoritiesConstants.USER)
    @GetMapping("/tags")
    public ResponseEntity<List<TagDTO>> getAll() {
        log.debug("REST request to get all tag");
        List<TagDTO> response = facade.getAll();
        return ResponseEntity.ok().body(response);
    }
}
