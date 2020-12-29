package com.jainshi.app.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

@Builder
@Data
public class ImageInfo {
    private String fileName;
    private String extension;
    private String absolutePath;
    private LocalDateTime takenOn;
    private LocalDateTime modifiedOn;
    private LocalDateTime createdOn;
    private String make;
    private String model;

    public String targetFolder() {
        return Optional.ofNullable(createdOn)
                .map(localDateTime -> String.format("%4d%02d", createdOn.getYear(), createdOn.getMonthValue()))
                .orElse("");
    }

}
