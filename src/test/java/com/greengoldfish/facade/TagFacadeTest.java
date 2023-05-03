package com.greengoldfish.facade;

import com.greengoldfish.domain.Tag;
import com.greengoldfish.facade.dto.tag.TagDTO;
import com.greengoldfish.facade.mapper.TagMapper;
import com.greengoldfish.service.TagService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class TagFacadeTest {

    private TagFacade facade;

    @Mock
    private TagService tagService;
    @Mock
    private TagMapper tagMapper;

    @BeforeEach
    void setUp() {
        facade = new TagFacade(
                tagMapper,
                tagService
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create() {
        Tag tag = new Tag();
        TagDTO tagDTO = new TagDTO();

        Mockito
                .when(tagService.findAll())
                .thenReturn(List.of(tag));
        Mockito
                .when(tagMapper.toDto(tag))
                .thenReturn(tagDTO);

        facade.findAll();

        Mockito
                .verify(tagService, Mockito.times(1))
                .findAll();
        Mockito
                .verify(tagMapper, Mockito.times(1))
                .toDto(tag);
    }
}