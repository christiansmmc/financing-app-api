package com.greengoldfish.facade.mapper;

import com.greengoldfish.domain.User;
import com.greengoldfish.facade.dto.user.UserDTO;
import com.greengoldfish.facade.dto.user.UserIdDTO;
import com.greengoldfish.facade.dto.user.UserToCreateDTO;
import com.greengoldfish.facade.dto.user.UserToGetDTO;
import com.greengoldfish.facade.dto.user.UserToUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends EntityMapper<UserDTO, User> {
    User toEntity(UserToCreateDTO dto);

    User toEntity(UserToUpdateDTO dto);

    UserIdDTO toIdDto(User user);

    UserToGetDTO toGetDto(User user);
}
