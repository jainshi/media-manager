package com.jainshi.app;

import com.jainshi.app.model.ImageInfo;
import com.jainshi.app.util.image.ImageInfoScanner;
import com.jainshi.app.util.io.IOUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class App 
{
    private static final String DEFAULT_SOURCE = "C:\\FileUtilTestData\\source";
    private static final String DEFAULT_TARGET = "C:\\FileUtilTestData\\target";
    private static final String[] DEFAULT_EXTENSIONS = new String[] {"jpg", "JPG", "MOV", "mov"};

    public static void main( String[] args ) {
        long start = System.currentTimeMillis();
        int argsLength = args.length;

        if (argsLength >=2) {
            log.info("Source: {}, Target: {}", args[0], args[1]);
            String sourceFolder = args[0];
            String targetBaseFolder = args[1] + "\\";
            boolean isMove = argsLength > 2 ? Boolean.valueOf(args[2]) : false;

            List<ImageInfo> images = ImageInfoScanner.scanImageInfo(sourceFolder, DEFAULT_EXTENSIONS);
            List<ImageInfo> failed = IOUtil.moveImageFiles(images, targetBaseFolder, isMove);
            log.info("Following files have not processed");
            failed.forEach(file -> log.info("ImageInfo: {}", file));
        } else {
            log.error("Source and target folders must be supplied as arguments: java -jar <jarFile> <sourceFolder> <targetFolder>");
        }
        log.info("Total time taken: {}", (System.currentTimeMillis() - start));
    }
}
