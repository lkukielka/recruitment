package com.learnifier.recruitment.controller;

import com.learnifier.recruitment.model.FileResource;
import com.learnifier.recruitment.model.Metadata;
import com.learnifier.recruitment.service.FileService;
import jdk.jfr.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {
    private static final String CONTENT_DISPOSITION_VALUE = "attachment; filename=\"%s\"";

    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Metadata> create(@RequestParam("file") MultipartFile file) {
        Metadata metadata = this.fileService.create(file);
        return ResponseEntity.ok(metadata);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ByteArrayResource> read(@PathVariable String id) {
        FileResource fileResource = this.fileService.read(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileResource.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, String.format(CONTENT_DISPOSITION_VALUE, fileResource.getOriginalName()))
                .body(fileResource.getResource());
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Metadata> update(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        Metadata metadata = this.fileService.update(id, file);
        return ResponseEntity.ok(metadata);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@PathVariable String id) {
        this.fileService.delete(id);
    }
}
