package com.greengoldfish.facade.mapper;

import com.greengoldfish.domain.Tag;
import com.greengoldfish.domain.User;
import com.greengoldfish.facade.dto.tag.TagDTO;
import com.greengoldfish.facade.dto.user.UserDTO;
import com.greengoldfish.facade.dto.user.UserIdDTO;
import com.greengoldfish.facade.dto.user.UserToCreateDTO;
import com.greengoldfish.facade.dto.user.UserToGetDTO;
import com.greengoldfish.facade.dto.user.UserToUpdateDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", uses = {}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper extends EntityMapper<TagDTO, Tag> {
}
