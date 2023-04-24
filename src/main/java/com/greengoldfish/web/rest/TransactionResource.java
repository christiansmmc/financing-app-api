package com.greengoldfish.web.rest;

import com.greengoldfish.facade.TransactionFacade;
import com.greengoldfish.facade.dto.transaction.TransactionIdDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToCreateDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class TransactionResource {

    private final Logger log = LoggerFactory.getLogger(TransactionResource.class);

    private final TransactionFacade facade;

    @PostMapping("/transactions")
    public ResponseEntity<TransactionIdDTO> create(@Valid @RequestBody TransactionToCreateDTO dto) throws URISyntaxException {
        log.debug("REST request to create transaction");
        TransactionIdDTO response = facade.create(dto);
        return ResponseEntity.created(new URI("/api/transaction/" + response.getId())).body(response);
    }
}
