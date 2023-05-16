package com.greengoldfish.facade.dto.creditcard;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardToUpdateDTO implements Serializable {

    @NotNull
    private Long id;

    @NotBlank
    private String identifier;

    @NotNull
    private Integer bestPurchaseDay;
}
