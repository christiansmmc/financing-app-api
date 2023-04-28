package com.greengoldfish.web.rest;

import com.greengoldfish.config.AuthoritiesConstants;
import com.greengoldfish.domain.enumeration.MonthType;
import com.greengoldfish.facade.TransactionFacade;
import com.greengoldfish.facade.dto.transaction.TransactionIdDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToCreateDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToGetDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToUpdateDTO;
import com.greengoldfish.web.rest.vm.TransactionSummaryVM;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class TransactionResource {

    private final TransactionFacade facade;

    @Secured(AuthoritiesConstants.USER)
    @PostMapping("/transactions")
    public ResponseEntity<TransactionIdDTO> create(
            @RequestBody @Valid TransactionToCreateDTO dto
    ) throws URISyntaxException {
        log.debug("REST request to create transaction");
        TransactionIdDTO response = facade.create(dto);
        return ResponseEntity.created(new URI("/api/transaction/" + response.getId())).body(response);
    }

    @Secured(AuthoritiesConstants.USER)
    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionToGetDTO>> findAll(
            @RequestParam(value = "initialDate", required = false) LocalDate initialDate,
            @RequestParam(value = "lastDate", required = false) LocalDate lastDate
    ) {
        log.debug("REST request to find all transactions");
        List<TransactionToGetDTO> response = facade.findAll(initialDate, lastDate);
        return ResponseEntity.ok().body(response);
    }

    @Secured(AuthoritiesConstants.USER)
    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionToGetDTO> findAll(@PathVariable Long id) {
        log.debug("REST request to find a transaction");
        TransactionToGetDTO response = facade.findById(id);
        return ResponseEntity.ok().body(response);
    }

    @Secured(AuthoritiesConstants.USER)
    @PatchMapping("/transactions/{id}")
    public ResponseEntity<TransactionIdDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid TransactionToUpdateDTO dto
    ) {
        log.debug("REST request to update a transaction");
        TransactionIdDTO response = facade.update(dto);
        return ResponseEntity.ok().body(response);
    }

    @Secured(AuthoritiesConstants.USER)
    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("REST request to delete a transaction");
        facade.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Secured(AuthoritiesConstants.USER)
    @GetMapping("/transactions/summary")
    public ResponseEntity<TransactionSummaryVM> summary(
            @RequestParam(value = "initialDate", required = false) LocalDate initialDate,
            @RequestParam(value = "lastDate", required = false) LocalDate lastDate,
            @RequestParam(value = "month", required = false) MonthType month
    ) {
        log.debug("REST request to get summary from transactions");
        TransactionSummaryVM response = facade.summary(initialDate, lastDate, month);
        return ResponseEntity.ok().body(response);
    }
}
