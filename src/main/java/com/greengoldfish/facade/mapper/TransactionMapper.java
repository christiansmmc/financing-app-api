package com.greengoldfish.facade.mapper;

import com.greengoldfish.domain.Transaction;
import com.greengoldfish.facade.dto.transaction.TransactionDTO;
import com.greengoldfish.facade.dto.transaction.TransactionIdDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToCreateDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToGetDTO;
import com.greengoldfish.facade.dto.transaction.TransactionToUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {
    Transaction toEntity(TransactionToCreateDTO dto);

    Transaction toEntity(TransactionToUpdateDTO dto);

    TransactionIdDTO toIdDto(Transaction transaction);

    TransactionToGetDTO toGetDto(Transaction transaction);
}
