package com.jainshi.app.model;

import com.drew.metadata.Directory;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.mov.QuickTimeDirectory;
import com.drew.metadata.mov.metadata.QuickTimeMetadataDirectory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

import static com.drew.metadata.exif.ExifDirectoryBase.*;
import static com.drew.metadata.mov.QuickTimeDirectory.TAG_CREATION_TIME;
import static com.drew.metadata.mov.QuickTimeDirectory.TAG_MODIFICATION_TIME;

@Getter
@AllArgsConstructor
public enum MediaType {

    IMAGE("image",
            ExifSubIFDDirectory.class, TAG_DATETIME_ORIGINAL, TAG_DATETIME_DIGITIZED,
            ExifIFD0Directory.class, TAG_MODEL, TAG_MAKE),
    VIDEO("video",
            QuickTimeDirectory.class, TAG_CREATION_TIME, TAG_MODIFICATION_TIME,
            QuickTimeMetadataDirectory.class, QuickTimeMetadataDirectory.TAG_MODEL, QuickTimeMetadataDirectory.TAG_MAKE);

    private String mimePrefix;
    private Class<? extends Directory> createModifiedTimeDirectory;
    private int createTimeTag;
    private int modifiedTimeTag;
    private Class<? extends Directory> makeModelDirectory;
    private int modelTag;
    private int makeTag;

    public static MediaType byMimeType(String mimeType) {
        return Optional.ofNullable(mimeType)
                .flatMap(
                        mime -> Arrays.stream(MediaType.values())
                                .filter(mediaType -> mime.startsWith(mediaType.getMimePrefix()))
                                .findFirst())
                .orElse(null);
    }
}
