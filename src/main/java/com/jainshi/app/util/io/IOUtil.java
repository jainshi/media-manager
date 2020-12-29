package com.jainshi.app.util.io;

import com.jainshi.app.model.ImageInfo;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class IOUtil {

    public List<ImageInfo> moveImageFiles(List<ImageInfo> imageInfoCollection, String destBaseFolder, boolean isMove) {
        final String movingOrCopyingLogValue = isMove ? "Moving" : "Copying";

        final List<ImageInfo> failedImages = imageInfoCollection.stream()
                .filter(file -> StringUtils.isBlank(file.targetFolder()))
                .collect(Collectors.toList());

        imageInfoCollection.stream()
                .filter(imageInfo -> StringUtils.isNotEmpty(imageInfo.targetFolder()))
                .forEach(imageInfo -> {
                    log.info("ImageInfo: {}", imageInfo);
                    final String sourceFile = imageInfo.getAbsolutePath();
                    final String targetFolder = destBaseFolder + imageInfo.targetFolder();
                    log.info("{} file: {} to folder: {}", movingOrCopyingLogValue, sourceFile, targetFolder);
                    try {
                        final File srcFile = new File(sourceFile);
                        final File destFile = new File(targetFolder);
                        if (isMove) {
                            FileUtils.moveFileToDirectory(srcFile, destFile, true);
                        } else {
                            FileUtils.copyFileToDirectory(srcFile, destFile, true);
                        }
                    } catch (FileExistsException e) {
                        log.info("File: {} already exist to folder: {}", sourceFile, targetFolder);
                        failedImages.add(imageInfo);
                    } catch (IOException e) {
                        log.error("Error while {} file: {} to folder: {}", movingOrCopyingLogValue, sourceFile, targetFolder, e);
                        failedImages.add(imageInfo);
                    }
                });
        return failedImages;
    }
}
