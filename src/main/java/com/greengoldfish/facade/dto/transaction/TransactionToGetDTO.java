package com.greengoldfish.facade.dto.transaction;

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
public class TransactionToGetDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private BigDecimal amount;

    private TransactionType type;

    private LocalDate date;

    private UserIdDTO user;
}
