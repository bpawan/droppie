package com.droppie.util.file;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class FileArchive {
    public Boolean zipFiles(List<String> downloadedFiles, String folderToCreateArchive, String zippedFileName) {

        if (downloadedFiles.size() == 0) {
            return false;
        }

        val optionalFileToWrite = this.createOutputStream(
                folderToCreateArchive + File.separator + zippedFileName);

        if (optionalFileToWrite.isPresent()) {
            val zipOutputStream = new ZipOutputStream(optionalFileToWrite.get());

            downloadedFiles.forEach(
                    srcFile -> addSourceFileToZip(folderToCreateArchive, zipOutputStream, srcFile)
            );

            try {
                zipOutputStream.close();
            } catch (IOException exception) {
                return false;
            }

            return true;
        }

        return false;
    }

    private void addSourceFileToZip(String folderToCreateArchive, ZipOutputStream zipOutputStream, String srcFile) {
        File fileToZip = new File(createFilePath(folderToCreateArchive, srcFile));

        try {
            val optionalInputStream = this.createFileInputStream(fileToZip);
            if (optionalInputStream.isPresent()) {
                val inputStream = optionalInputStream.get();
                writeBytesOnZipFile(zipOutputStream, inputStream, fileToZip.getName());
                inputStream.close();
            }
        } catch (IOException exception) {
            log.error("Failed writing data to zip file.", exception);
        }
    }

    @NotNull
    private String createFilePath(String folderToCreateArchive, String srcFile) {
        return folderToCreateArchive + File.separator + this.createFileName(srcFile);
    }

    private void writeBytesOnZipFile(ZipOutputStream zipOutputStream, FileInputStream inputStream, String zipFileName)
            throws IOException {

        val zipEntry = new ZipEntry(zipFileName);

        zipOutputStream.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = inputStream.read(bytes)) >= 0) {
            zipOutputStream.write(bytes, 0, length);
        }
    }

    private Optional<FileOutputStream> createOutputStream(String filePath) {
        try {
            val fileOutputStream = new FileOutputStream(filePath);
            return Optional.of(fileOutputStream);
        } catch (FileNotFoundException exception) {
            log.error(String.format("Specified zip file was not found (%s)", filePath), exception);

            return Optional.empty();
        }
    }

    private Optional<FileInputStream> createFileInputStream(File file) {
        try {
            return Optional.of(new FileInputStream(file));
        } catch (FileNotFoundException exception) {
            log.error(String.format("Specified file to add to zip was not found (%s)", file.getName()), exception);

            return Optional.empty();
        }
    }

    private String createFileName(String filename) {
        return Paths.get(filename).getFileName().toString();
    }
}
