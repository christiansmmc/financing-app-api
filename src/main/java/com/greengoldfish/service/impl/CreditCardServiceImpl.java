package com.greengoldfish.service.impl;

import com.greengoldfish.domain.CreditCard;
import com.greengoldfish.domain.User;
import com.greengoldfish.exception.BusinessException;
import com.greengoldfish.exception.enumerations.ErrorConstants;
import com.greengoldfish.repository.CreditCardRepository;
import com.greengoldfish.service.CreditCardService;
import com.greengoldfish.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository repository;
    private final UserService userService;

    @Override
    public CreditCard create(CreditCard creditCard) {
        User loggedUser = userService.findLoggedUser();

        creditCard.setUser(loggedUser);
        return repository.save(creditCard);
    }

    @Override
    public CreditCard update(CreditCard creditCard) {
        User loggedUser = userService.findLoggedUser();
        CreditCard creditCardToUpdate = repository.findById(creditCard.getId())
                .orElseThrow(BusinessException.notFound(CreditCard.class));

        BusinessException.throwIfNot(
                creditCardToUpdate.getUser().equals(loggedUser),
                ErrorConstants.CREDIT_CARD_NOT_FROM_USER
        );

        creditCardToUpdate.setIdentifier(creditCard.getIdentifier());
        creditCardToUpdate.setBestPurchaseDay(creditCard.getBestPurchaseDay());
        return repository.save(creditCardToUpdate);
    }

    @Override
    public void delete(Long id) {
        User loggedUser = userService.findLoggedUser();
        CreditCard creditCard = repository.findById(id)
                .orElseThrow(BusinessException.notFound(CreditCard.class));

        BusinessException.throwIfNot(
                creditCard.getUser().equals(loggedUser),
                ErrorConstants.CREDIT_CARD_NOT_FROM_USER
        );

        repository.delete(creditCard);
    }

    @Override
    public List<CreditCard> findAllFromLoggedUser() {
        User loggedUser = userService.findLoggedUser();
        return repository.findAllByUser(loggedUser);
    }

    @Override
    public CreditCard findById(Long id) {
        return repository.findById(id)
                .orElseThrow(BusinessException.notFound(CreditCard.class));
    }
}
