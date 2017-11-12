package com.github.adminfaces.showcase.analytics.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {

    public static final Logger log = LoggerFactory.getLogger(FileUtils.class.getName());


    public static void unzip(InputStream zipFile, String targetDir) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(zipFile);
        try {
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            while (zipEntry != null) {
                File destPath = new File(targetDir);
                log.info("Unpacking {}.", destPath.getAbsoluteFile());
                if (!zipEntry.isDirectory()) {
                    FileOutputStream fout = new FileOutputStream(destPath);
                    final byte[] buffer = new byte[8192];
                    int n = 0;
                    while (-1 != (n = zipInputStream.read(buffer))) {
                        fout.write(buffer, 0, n);
                    }
                    fout.close();
                } else {
                    destPath.mkdir();
                }
                zipEntry = zipInputStream.getNextEntry();
            }
        } finally {
            zipInputStream.close();
        }
    }
}
