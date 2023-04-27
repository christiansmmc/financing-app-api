package com.greengoldfish.facade;

import com.greengoldfish.domain.User;
import com.greengoldfish.facade.dto.user.UserIdDTO;
import com.greengoldfish.facade.dto.user.UserToCreateDTO;
import com.greengoldfish.facade.dto.user.UserToGetDTO;
import com.greengoldfish.facade.dto.user.UserToUpdateDTO;
import com.greengoldfish.facade.mapper.UserMapper;
import com.greengoldfish.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserFacade {

    private final UserMapper mapper;
    private final UserService service;

    @Transactional
    public UserIdDTO create(UserToCreateDTO dto) {
        User user = mapper.toEntity(dto);
        return mapper.toIdDto(service.create(user));
    }

    @Transactional
    public UserIdDTO update(UserToUpdateDTO dto) {
        User user = mapper.toEntity(dto);
        return mapper.toIdDto(service.update(user));
    }

    @Transactional
    public UserToGetDTO getInfoByLoggedUser() {
        return mapper.toGetDto(service.getLoggedUser());
    }
}
