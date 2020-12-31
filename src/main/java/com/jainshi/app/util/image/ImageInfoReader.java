package com.jainshi.app.util.image;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.file.FileTypeDirectory;
import com.jainshi.app.model.ImageInfo;
import com.jainshi.app.model.MediaType;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static com.drew.metadata.file.FileTypeDirectory.TAG_DETECTED_FILE_MIME_TYPE;
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

        getMetadata(imagePath)
                .ifPresent(meta -> {
                    String mimeType = extractString(meta, FileTypeDirectory.class, TAG_DETECTED_FILE_MIME_TYPE);
                    imageInfo.setExtension(extractString(meta, FileTypeDirectory.class, TAG_EXPECTED_FILE_NAME_EXTENSION));

                    Optional.ofNullable(MediaType.byMimeType(mimeType))
                            .ifPresentOrElse(mediaType -> {
                                imageInfo.setCreatedOn(asLocalDateTime(extractDate(meta,
                                        mediaType.getCreateModifiedTimeDirectory(),
                                        mediaType.getCreateTimeTag())));
                                imageInfo.setModifiedOn(asLocalDateTime(extractDate(meta,
                                        mediaType.getCreateModifiedTimeDirectory(),
                                        mediaType.getModifiedTimeTag())));
                                imageInfo.setMake(extractString(meta,
                                        mediaType.getMakeModelDirectory(),
                                        mediaType.getMakeTag()));
                                imageInfo.setModel(extractString(meta,
                                        mediaType.getMakeModelDirectory(),
                                        mediaType.getModelTag()));
                            }, () ->  log.warn("Unknown mime type: {}", mimeType) );

                });

        return imageInfo;
    }

    private static Optional<Metadata> getMetadata(File imagePath) {
        try {
            return Optional.ofNullable(ImageMetadataReader.readMetadata(imagePath));
        } catch (ImageProcessingException | IOException e) {
            log.error("Metadata could not be extract for file: {}", imagePath, e);
            return Optional.empty();
        }
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
