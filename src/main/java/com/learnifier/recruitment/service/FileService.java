package com.learnifier.recruitment.service;

import com.learnifier.recruitment.exception.BadRequestException;
import com.learnifier.recruitment.exception.NotFoundException;
import com.learnifier.recruitment.model.FileResource;
import com.learnifier.recruitment.model.Metadata;
import com.learnifier.recruitment.repository.MetadataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileService {

    private final String fileStorageLocation;
    private final MetadataRepository metadataRepository;

    @Autowired
    public FileService(@Value("${file.storage.location}") String fileStorageLocation, MetadataRepository metadataRepository) {
        this.fileStorageLocation = fileStorageLocation;
        this.metadataRepository = metadataRepository;
    }

    @Transactional
    public Metadata create(MultipartFile file) {
        if (!AcceptedContentType.validContentType(file.getContentType())) {
            throw new BadRequestException("Incorrect file type");
        }

        Metadata metadata = metadataRepository.save(Metadata.builder()
                .originalName(file.getOriginalFilename())
                .contentType(file.getContentType())
                .size(file.getSize()).build());
        storeFile(file, metadata);
        return metadata;
    }

    public FileResource read(String id) {
        Metadata metadata = this.metadataRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new NotFoundException("Can't find file with id " + id));
        ByteArrayResource resource = getFile(metadata);
        return FileResource.builder()
                .size(metadata.getSize())
                .originalName(metadata.getOriginalName())
                .contentType(metadata.getContentType())
                .resource(resource).build();
    }

    @Transactional
    public Metadata update(String id, MultipartFile file) {
        Metadata metadata = this.metadataRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new NotFoundException("Can't find file with id " + id));

        if (!metadata.getContentType().equalsIgnoreCase(file.getContentType())) {
            throw new BadRequestException("Updating file has to be the same type as original.");
        }

        deleteFile(metadata);
        storeFile(file, metadata);
        metadata.setOriginalName(file.getOriginalFilename())
                .setSize(file.getSize());
        return this.metadataRepository.save(metadata);
    }

    public void delete(String id) {
        Metadata metadata = this.metadataRepository.findById(Long.parseLong(id))
                .orElseThrow(() -> new NotFoundException("Can't find file with id " + id));
        deleteFile(metadata);
        this.metadataRepository.delete(metadata);
    }

    private void storeFile(MultipartFile file, Metadata metadata) {
        try {
            InputStream is = file.getInputStream();
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            File targetFile = new File(getFileLocation(metadata));
            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            try (OutputStream out = new FileOutputStream(targetFile)) {
                out.write(buffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private ByteArrayResource getFile(Metadata metadata) {
        ByteArrayResource file;
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(getFileLocation(metadata)));
            file = new ByteArrayResource(fileBytes);
            if (!file.exists()) {
                throw new NotFoundException("File not found.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return file;
    }

    private void deleteFile(Metadata metadata) {
        try {
            Files.deleteIfExists(Paths.get(getFileLocation(metadata)));
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private String getFileLocation(Metadata metadata) {
        return this.fileStorageLocation + metadata.getId() + "/" + metadata.getOriginalName();
    }
}
