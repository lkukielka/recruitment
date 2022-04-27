package com.learnifier.recruitment.controller;

import com.learnifier.recruitment.model.Metadata;
import com.learnifier.recruitment.service.FileMetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/files/metadata")
public class FileMetadataController {
    private final FileMetadataService fileMetadataService;

    @Autowired
    public FileMetadataController(FileMetadataService fileMetadataService) {
        this.fileMetadataService = fileMetadataService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Metadata>> getAll() {
        List<Metadata> metadatas = this.fileMetadataService.getAllMetadata();
        return ResponseEntity.ok(metadatas);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Metadata> getById(@PathVariable String id) {
        Metadata metadata = this.fileMetadataService.getById(id);
        return ResponseEntity.ok(metadata);
    }

}
