package com.greengoldfish.service;

import com.greengoldfish.domain.User;

public interface UserService {
    User create(User user);

    User findLoggedUser();

    User update(User user);
}
