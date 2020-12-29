package com.jainshi.app.util.image;

import com.jainshi.app.model.ImageInfo;
import lombok.experimental.UtilityClass;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@UtilityClass
public class ImageInfoScanner {

    public static List<ImageInfo> scanImageInfo(String baseFolder, String... fileExtensions) {
        final List<ImageInfo> imageInfoCollection = new ArrayList<>();
        final File base = new File(baseFolder);
        if (base.exists() && base.isDirectory()) {
            FileUtils.listFiles(base, fileExtensions, true)
                    .parallelStream()
                    .filter(file -> file.isFile())
                    .forEach(file -> imageInfoCollection.add(
                            ImageInfoReader.extractImageInfo(file)));
            return imageInfoCollection;
        } else {
            return Collections.emptyList();
        }
    }

}
