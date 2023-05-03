package com.greengoldfish.facade.dto.transaction;

import com.greengoldfish.domain.enumeration.TransactionType;
import com.greengoldfish.facade.dto.tag.TagIdDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TransactionToUpdateDTO implements Serializable {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private TransactionType type;

    @NotNull
    private LocalDate date;

    @Valid
    private TagIdDTO tag;
}
