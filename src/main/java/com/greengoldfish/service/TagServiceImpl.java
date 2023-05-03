package com.greengoldfish.service;

import com.greengoldfish.domain.Tag;
import com.greengoldfish.exception.BusinessException;
import com.greengoldfish.repository.TagRepository;
import com.greengoldfish.service.TagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository repository;

    @Override
    public List<Tag> getAll() {
        return repository.findAll();
    }

    @Override
    public Tag findById(Long id) {
        return repository.findById(id)
                .orElseThrow(BusinessException.notFound(Tag.class));
    }
}
