package com.greengoldfish.facade;

import com.greengoldfish.facade.dto.tag.TagDTO;
import com.greengoldfish.facade.mapper.TagMapper;
import com.greengoldfish.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TagFacade {

    private final TagMapper mapper;
    private final TagService service;

    @Transactional(readOnly = true)
    public List<TagDTO> getAll() {
        return service.getAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
