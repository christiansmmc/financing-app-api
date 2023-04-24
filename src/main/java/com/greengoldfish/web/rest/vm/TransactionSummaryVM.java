package com.greengoldfish.web.rest.vm;

import com.greengoldfish.domain.enumeration.TransactionType;
import com.greengoldfish.facade.dto.user.UserIdDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionSummaryVM implements Serializable {

    private LocalDate initialDate;

    private LocalDate lastDate;

    private BigDecimal totalOutcome;

    private BigDecimal totalIncome;
}
