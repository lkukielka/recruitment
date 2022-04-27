package com.learnifier.recruitment.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;

@Getter
@Setter
@Builder
public class FileResource {
    private long size;
    private String originalName;
    private String contentType;
    private ByteArrayResource resource;
}
