package com.greengoldfish.service;

import com.greengoldfish.domain.CreditCard;

import java.util.List;

public interface CreditCardService {
    CreditCard create(CreditCard creditCard);

    CreditCard update(CreditCard creditCard);

    void delete(Long id);

    List<CreditCard> findAllFromLoggedUser();
}
