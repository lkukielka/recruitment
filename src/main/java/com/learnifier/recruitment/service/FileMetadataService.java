package com.learnifier.recruitment.service;

import com.learnifier.recruitment.exception.NotFoundException;
import com.learnifier.recruitment.model.Metadata;
import com.learnifier.recruitment.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FileMetadataService {

    private MetadataRepository metadataRepository;

    @Autowired
    public FileMetadataService(MetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    public Metadata getById(String id) {
        return this.metadataRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new NotFoundException("Can't find metadata with id " + id));
    }

    public List<Metadata> getAllMetadata() {
        return StreamSupport.stream(this.metadataRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }
}
