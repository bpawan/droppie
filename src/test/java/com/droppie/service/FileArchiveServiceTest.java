package com.droppie.service;

import lombok.val;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

@DisplayName("File Archive Service")
class FileArchiveServiceTest {

    private static final String DOWNLOAD_FOLDER = "files/download/";

    private static final String ZIPPED_FILE_NAME = "download.zip";

    private FileArchiveService fileArchiveService = new FileArchiveService();

    @AfterEach()
    void cleanup() throws IOException {
        FileUtils.deleteDirectory(new File(DOWNLOAD_FOLDER));
    }

    @Test
    @DisplayName("Create zipped file for provided input files.")
    void createZipFile() throws Exception {
        this.fileArchiveService.zipFiles(this.createTestFilesToZip(), DOWNLOAD_FOLDER, ZIPPED_FILE_NAME);



    }

    List<String> createTestFilesToZip() {
        val files = List.of("file1.txt", "file2.txt");

        files.forEach(fileName -> new File(DOWNLOAD_FOLDER + "/" + fileName));

        return files;
    }
}
