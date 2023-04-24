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
public class TransactionIdDTO implements Serializable {

    private Long id;
}
