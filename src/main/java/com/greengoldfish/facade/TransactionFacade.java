package com.greengoldfish.facade;

import com.greengoldfish.domain.Transaction;
import com.greengoldfish.domain.User;
import com.greengoldfish.facade.dto.transaction.TransactionDTO;
import com.greengoldfish.facade.dto.transaction.TransactionIdDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToCreateDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToGetDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToUpdateDTO;
import com.greengoldfish.facade.dto.user.UserIdDTO;
import com.greengoldfish.facade.dto.user.UserToCreateDTO;
import com.greengoldfish.facade.mapper.TransactionMapper;
import com.greengoldfish.facade.mapper.UserMapper;
import com.greengoldfish.service.TransactionService;
import com.greengoldfish.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionFacade {

    private final TransactionMapper mapper;
    private final TransactionService service;

    @Transactional
    public TransactionIdDTO create(TransactionToCreateDTO dto) {
        Transaction transaction = mapper.toEntity(dto);
        return mapper.toIdDto(service.create(transaction));
    }

    @Transactional
    public List<TransactionToGetDTO> findAll() {
        return service.findAll().stream().map(mapper::toGetDto).collect(Collectors.toList());
    }

    @Transactional
    public TransactionToGetDTO findById(Long id) {
        return mapper.toGetDto(service.findById(id));
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
}
