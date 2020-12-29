package com.jainshi.app;

import com.jainshi.app.model.ImageInfo;
import com.jainshi.app.util.image.ImageInfoScanner;
import com.jainshi.app.util.io.IOUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Hello world!
 *
 */
@Slf4j
public class App 
{
    public static void main( String[] args ) {
        String sourceFolder = "C:\\FileUtilTestData";
        String targetBaseFolder = "C:\\FileUtilTestTarget\\";
        String[] extensions = new String[] {"jpg", "JPG"};
        List<ImageInfo> images = ImageInfoScanner.scanImageInfo(sourceFolder, null);
        List<ImageInfo> failed = IOUtil.moveImageFiles(images, targetBaseFolder, true);
        log.info("Following files have not processed");
        failed.forEach(file -> log.info("ImageInfo: {}", file));
    }
}
