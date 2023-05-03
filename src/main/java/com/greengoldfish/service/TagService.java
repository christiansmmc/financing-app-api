package com.greengoldfish.service;

import com.greengoldfish.domain.Tag;

import java.util.List;

public interface TagService {

    List<Tag> findAll();

    Tag findById(Long id);
}
