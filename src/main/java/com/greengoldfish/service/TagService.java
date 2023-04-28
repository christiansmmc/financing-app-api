package com.greengoldfish.service;

import com.greengoldfish.domain.Tag;

import java.util.List;

public interface TagService {

    List<Tag> getAll();

    Tag findById(Long id);
}
