package com.greengoldfish.repository;

import com.greengoldfish.domain.CreditCard;
import com.greengoldfish.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    List<CreditCard> findAllByUser(User user);
}
