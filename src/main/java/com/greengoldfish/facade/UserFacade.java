package com.greengoldfish.facade;

import com.greengoldfish.domain.User;
import com.greengoldfish.facade.dto.UserIdDTO;
import com.greengoldfish.facade.dto.UserToCreateDTO;
import com.greengoldfish.facade.mapper.UserMapper;
import com.greengoldfish.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserFacade {

    private final UserMapper mapper;
    private final UserService service;

    public UserFacade(
            UserMapper mapper,
            UserService service
    ) {
        this.mapper = mapper;
        this.service = service;
    }

    @Transactional
    public UserIdDTO create(UserToCreateDTO dto) {
        User user = mapper.toEntity(dto);
        return mapper.toIdDto(service.create(user));
    }
}
