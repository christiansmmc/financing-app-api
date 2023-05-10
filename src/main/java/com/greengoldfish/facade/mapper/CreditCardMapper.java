package com.greengoldfish.facade.mapper;

import com.greengoldfish.domain.CreditCard;
import com.greengoldfish.facade.dto.creditcard.CreditCardDTO;
import com.greengoldfish.facade.dto.creditcard.CreditCardIdDTO;
import com.greengoldfish.facade.dto.creditcard.CreditCardSimpleDTO;
import com.greengoldfish.facade.dto.creditcard.CreditCardToCreateDTO;
import com.greengoldfish.facade.dto.creditcard.CreditCardToUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CreditCardMapper extends EntityMapper<CreditCardDTO, CreditCard> {

    CreditCard toEntity(CreditCardToCreateDTO dto);

    CreditCard toEntity(CreditCardToUpdateDTO dto);

    CreditCardIdDTO toIdDto(CreditCard creditCard);

    CreditCardSimpleDTO toSimpleDto(CreditCard creditCard);
}
