package com.greengoldfish.service;

import com.greengoldfish.domain.Tag;
import com.greengoldfish.domain.Tag.TagBuilder;
import com.greengoldfish.exception.BusinessException;
import com.greengoldfish.repository.TagRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

@ExtendWith(MockitoExtension.class)
class TagServiceTest {

    private TagService tagService;

    @Mock
    private TagRepository repository;

    Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        tagService = new TagServiceImpl(
                repository
        );
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAll() {
        Tag tag = createTag(UnaryOperator.identity());

        Mockito
                .when(repository.findAll())
                .thenReturn(List.of(tag));

        tagService.findAll();

        Mockito
                .verify(repository, Mockito.times(1))
                .findAll();
    }

    @Test
    void findById() {
        Tag tag = createTag(UnaryOperator.identity());

        Mockito
                .when(repository.findById(tag.getId()))
                .thenReturn(Optional.of(tag));

        tagService.findById(tag.getId());

        Mockito
                .verify(repository, Mockito.times(1))
                .findById(tag.getId());
    }

    @Test
    void findById_notFound() {
        Long fakeTagId = faker.number().randomNumber();

        Mockito
                .when(repository.findById(fakeTagId))
                .thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class, () -> {
            tagService.findById(fakeTagId);
        });

        Mockito
                .verify(repository, Mockito.times(1))
                .findById(fakeTagId);
    }

    private Tag createTag(UnaryOperator<TagBuilder> builder) {
        TagBuilder tagBuilder = Tag
                .builder()
                .id(faker.number().randomNumber())
                .name(faker.eldenRing().npc());

        return builder.apply(tagBuilder).build();
    }
}