package com.greengoldfish.facade;

import com.greengoldfish.domain.Transaction;
import com.greengoldfish.domain.enumeration.MonthType;
import com.greengoldfish.facade.dto.transaction.TransactionDTO;
import com.greengoldfish.facade.dto.transaction.TransactionIdDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToCreateDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToGetDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToUpdateDTO;
import com.greengoldfish.facade.mapper.TransactionMapper;
import com.greengoldfish.service.TransactionService;
import com.greengoldfish.web.rest.vm.TransactionSummaryVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TransactionFacade {

    private final TransactionMapper mapper;
    private final TransactionService service;

    @Transactional
    public TransactionDTO create(TransactionToCreateDTO dto) {
        Transaction transaction = mapper.toEntity(dto);
        return mapper.toDto(service.create(transaction));
    }

    @Transactional(readOnly = true)
    public List<TransactionToGetDTO> findAllByLoggedUser(LocalDate initialDate, LocalDate lastDate) {
        return service.findAllByLoggedUser(initialDate, lastDate)
                .stream()
                .map(mapper::toGetDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TransactionToGetDTO findById(Long id) {
        return mapper.toGetDto(service.findByIdAndLoggedUser(id));
    }

    @Transactional
    public TransactionIdDTO update(TransactionToUpdateDTO dto) {
        Transaction transaction = mapper.toEntity(dto);
        return mapper.toIdDto(service.update(transaction));
    }

    @Transactional
    public void delete(Long id) {
        service.delete(id);
    }

    @Transactional(readOnly = true)
    public TransactionSummaryVM summary(LocalDate initialDate, LocalDate lastDate, MonthType month) {
        return service.summary(initialDate, lastDate, month);
    }
}
