package com.greengoldfish.facade.dto.creditcard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardSimpleDTO implements Serializable {

    private Long id;

    private String identifier;

    private Integer bestPurchaseDay;
}
