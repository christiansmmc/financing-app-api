package com.greengoldfish.facade;

import com.greengoldfish.domain.CreditCard;
import com.greengoldfish.facade.dto.creditcard.CreditCardIdDTO;
import com.greengoldfish.facade.dto.creditcard.CreditCardSimpleDTO;
import com.greengoldfish.facade.dto.creditcard.CreditCardToCreateDTO;
import com.greengoldfish.facade.dto.creditcard.CreditCardToUpdateDTO;
import com.greengoldfish.facade.mapper.CreditCardMapper;
import com.greengoldfish.service.CreditCardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CreditCardFacade {

    private final CreditCardMapper mapper;
    private final CreditCardService service;

    @Transactional
    public CreditCardIdDTO create(CreditCardToCreateDTO dto) {
        CreditCard creditCard = mapper.toEntity(dto);
        return mapper.toIdDto(service.create(creditCard));
    }

    @Transactional
    public CreditCardIdDTO update(CreditCardToUpdateDTO dto) {
        CreditCard creditCard = mapper.toEntity(dto);
        return mapper.toIdDto(service.update(creditCard));
    }

    @Transactional
    public void delete(Long id) {
        service.delete(id);
    }

    @Transactional(readOnly = true)
    public List<CreditCardSimpleDTO> findAllByLoggedUser() {
        return service.findAllFromLoggedUser()
                .stream()
                .map(mapper::toSimpleDto)
                .collect(Collectors.toList());
    }
}
