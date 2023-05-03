package com.greengoldfish.facade;

import com.greengoldfish.domain.User;
import com.greengoldfish.facade.dto.user.UserIdDTO;
import com.greengoldfish.facade.dto.user.UserToCreateDTO;
import com.greengoldfish.facade.dto.user.UserToGetDTO;
import com.greengoldfish.facade.dto.user.UserToUpdateDTO;
import com.greengoldfish.facade.mapper.UserMapper;
import com.greengoldfish.service.UserService;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserFacadeTest {

    private UserFacade facade;

    @Mock
    private UserService userService;
    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        facade = new UserFacade(
                userMapper,
                userService
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
        UserToCreateDTO toCreateDTO = new UserToCreateDTO();
        UserIdDTO idDTO = new UserIdDTO();
        User user = new User();

        Mockito.when(userMapper.toEntity(toCreateDTO))
                .thenReturn(user);
        Mockito.when(userService.create(user))
                .thenReturn(user);
        Mockito.when(userMapper.toIdDto(user))
                .thenReturn(idDTO);

        facade.create(toCreateDTO);

        Mockito.verify(userMapper, Mockito.times(1))
                .toEntity(toCreateDTO);
        Mockito.verify(userService, Mockito.times(1))
                .create(user);
        Mockito.verify(userMapper, Mockito.times(1))
                .toIdDto(user);
    }

    @Test
    void update() {
        UserToUpdateDTO toUpdateDTO = new UserToUpdateDTO();
        UserIdDTO idDTO = new UserIdDTO();
        User user = new User();

        Mockito.when(userMapper.toEntity(toUpdateDTO))
                .thenReturn(user);
        Mockito.when(userService.update(user))
                .thenReturn(user);
        Mockito.when(userMapper.toIdDto(user))
                .thenReturn(idDTO);

        facade.update(toUpdateDTO);

        Mockito.verify(userMapper, Mockito.times(1))
                .toEntity(toUpdateDTO);
        Mockito.verify(userService, Mockito.times(1))
                .update(user);
        Mockito.verify(userMapper, Mockito.times(1))
                .toIdDto(user);
    }

    @Test
    void getInfoByLoggedUser() {
        UserToGetDTO toGetDTO = new UserToGetDTO();
        User user = new User();

        Mockito.when(userService.findLoggedUser())
                .thenReturn(user);
        Mockito.when(userMapper.toGetDto(user))
                .thenReturn(toGetDTO);

        facade.getInfoByLoggedUser();

        Mockito.verify(userService, Mockito.times(1))
                .findLoggedUser();
        Mockito.verify(userMapper, Mockito.times(1))
                .toGetDto(user);
    }
}