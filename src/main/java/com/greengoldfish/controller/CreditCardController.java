package com.greengoldfish.controller;

import com.greengoldfish.config.AuthoritiesConstants;
import com.greengoldfish.facade.CreditCardFacade;
import com.greengoldfish.facade.dto.creditcard.CreditCardIdDTO;
import com.greengoldfish.facade.dto.creditcard.CreditCardSimpleDTO;
import com.greengoldfish.facade.dto.creditcard.CreditCardToCreateDTO;
import com.greengoldfish.facade.dto.creditcard.CreditCardToUpdateDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class CreditCardController {

    private final CreditCardFacade facade;

    @Secured(AuthoritiesConstants.USER)
    @PostMapping("/credit-cards")
    public ResponseEntity<CreditCardIdDTO> create(
            @RequestBody @Valid CreditCardToCreateDTO dto
    ) throws URISyntaxException {
        log.debug("REST request to create credit card: {}", dto);
        CreditCardIdDTO response = facade.create(dto);
        return ResponseEntity.created(new URI("/api/credit-cards/" + response.getId())).body(response);
    }

    @Secured(AuthoritiesConstants.USER)
    @PatchMapping("/credit-cards/{id}")
    public ResponseEntity<CreditCardIdDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid CreditCardToUpdateDTO dto
    ) {
        log.debug("REST request to update credit card: {}", id);
        CreditCardIdDTO response = facade.update(dto);
        return ResponseEntity.ok().body(response);
    }

    @Secured(AuthoritiesConstants.USER)
    @DeleteMapping("/credit-cards/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete credit card: {}", id);
        facade.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Secured(AuthoritiesConstants.USER)
    @DeleteMapping("/credit-cards")
    public ResponseEntity<List<CreditCardSimpleDTO>> findAllByLoggedUser() {
        log.debug("REST request to get all credit card by logged user");
        List<CreditCardSimpleDTO> response = facade.findAllByLoggedUser();
        return ResponseEntity.ok().body(response);
    }
}
