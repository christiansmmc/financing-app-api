package com.greengoldfish.facade.mapper;

import com.greengoldfish.domain.User;
import com.greengoldfish.facade.dto.UserDTO;
import com.greengoldfish.facade.dto.UserIdDTO;
import com.greengoldfish.facade.dto.UserToCreateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends EntityMapper<UserDTO, User> {
    User toEntity(UserToCreateDTO dto);

    UserIdDTO toIdDto(User user);
}
