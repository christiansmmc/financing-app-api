package com.greengoldfish.facade.dto.creditcard;

import com.greengoldfish.facade.dto.user.UserIdDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardDTO implements Serializable {

    private Long id;

    private String identifier;

    private Integer bestPurchaseDay;

    private UserIdDTO user;
}
