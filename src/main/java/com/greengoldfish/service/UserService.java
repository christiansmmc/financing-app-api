package com.greengoldfish.service;

import com.greengoldfish.domain.User;

public interface UserService {
    User create(User user);

    User getLoggedUser();

    User update(User user);

    User getByLoggedUser();
}
