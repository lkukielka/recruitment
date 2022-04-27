package com.learnifier.recruitment.service;

import java.util.Arrays;

public enum AcceptedContentType {
    VIDEO("video/mp4"),
    IMAGE("image/jpeg"),
    TEXT("text/plain");

    private String[] contentType;

    AcceptedContentType(String... fileTypeExtensions) {
        this.contentType = fileTypeExtensions;
    }

    public String[] getContentType() {
        return this.contentType;
    }

    public static boolean validContentType(String type) {
        return Arrays.stream(AcceptedContentType.class.getEnumConstants())
                .flatMap(fileType -> Arrays.stream(fileType.getContentType()))
                .anyMatch(ct -> ct.equalsIgnoreCase(type));
    }
}
