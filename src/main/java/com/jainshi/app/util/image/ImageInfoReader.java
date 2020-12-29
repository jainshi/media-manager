package com.jainshi.app.util.image;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.file.FileTypeDirectory;
import com.jainshi.app.model.ImageInfo;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static com.drew.metadata.exif.ExifDirectoryBase.*;
import static com.drew.metadata.file.FileTypeDirectory.TAG_EXPECTED_FILE_NAME_EXTENSION;

@Slf4j
@UtilityClass
public class ImageInfoReader {

    @SneakyThrows
    public static ImageInfo extractImageInfo(File imagePath) {
        log.info("extracting image info for file: {}", imagePath);

        ImageInfo imageInfo = ImageInfo.builder()
                .absolutePath(imagePath.getAbsolutePath())
                .fileName(imagePath.getName())
                .build();

        Metadata metadata = ImageMetadataReader.readMetadata(imagePath);

        Optional.ofNullable(metadata)
                .ifPresent(meta -> {
                    imageInfo.setCreatedOn(asLocalDateTime(extractDate(meta, ExifSubIFDDirectory.class, TAG_DATETIME_ORIGINAL)));
                    imageInfo.setModifiedOn(asLocalDateTime(extractDate(meta, ExifSubIFDDirectory.class, TAG_DATETIME_DIGITIZED)));
                    imageInfo.setModel(extractString(meta, ExifIFD0Directory.class, TAG_MODEL));
                    imageInfo.setMake(extractString(meta, ExifIFD0Directory.class, TAG_MAKE));
                    imageInfo.setExtension(extractString(meta, FileTypeDirectory.class, TAG_EXPECTED_FILE_NAME_EXTENSION));
                });

        return imageInfo;
    }

    private <D extends Directory> String extractString(Metadata meta, Class<D> directoryClass, int tagExtension) {
        return Optional.ofNullable(meta.getFirstDirectoryOfType(directoryClass))
                .flatMap(directory ->
                        Optional.ofNullable(directory.getString(tagExtension)))
                .orElse(null);
    }

    private <D extends Directory> Date extractDate(Metadata meta, Class<D> directoryClass, int tagExtension) {
        return Optional.ofNullable(meta.getFirstDirectoryOfType(directoryClass))
                .flatMap(directory ->
                        Optional.ofNullable(directory.getDate(tagExtension)))
                .orElse(null);
    }

    private static LocalDateTime asLocalDateTime(Date date) {
        return Optional.ofNullable(date)
                .map(d -> LocalDateTime.ofInstant(d.toInstant(), ZoneId.systemDefault()))
                .orElse(null);
    }
}
