package com.jainshi.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
@AllArgsConstructor
public enum MediaType {

    IMAGE("image"),
    VIDEO("video"),
    UNKNOWN("unknown");

    private String mimePrefix;

    public static MediaType byMimeType(String mimeType) {
        return Optional.ofNullable(mimeType)
                .flatMap(
                        mime -> Arrays.stream(MediaType.values())
                                .filter(mediaType -> mime.startsWith(mediaType.getMimePrefix()))
                                .findFirst())
                .orElse(UNKNOWN);
    }
}
