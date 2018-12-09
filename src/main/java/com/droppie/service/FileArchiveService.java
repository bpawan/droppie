package com.droppie.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileArchiveService {
    public String zipFiles(List<String> downloadedFiles, String folderToCreateArchive, String zippedFileName) throws IOException {

        FileOutputStream fos = new FileOutputStream(zippedFileName);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        for (String srcFile : downloadedFiles) {
            File fileToZip = new File(folderToCreateArchive + this.createFileName(srcFile));
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            fis.close();
        }
        zipOut.close();

        return zippedFileName;
    }

    private String createFileName(String filename) {
        return Paths.get(filename).getFileName().toString();
    }
}
