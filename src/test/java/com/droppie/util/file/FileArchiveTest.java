package com.droppie.util.file;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import lombok.val;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.google.common.io.Files.asCharSink;
import static com.google.common.io.Files.createParentDirs;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("File Archive Tests")
class FileArchiveTest {

    private static final String DOWNLOAD_FOLDER = "files/archive";

    private static final String ZIPPED_FILE_NAME = "download.zip";

    private static final List<String> filesToZip = List.of("/file1.txt", "/file2.txt");

    private static final Logger logger = (Logger) LoggerFactory.getLogger(FileArchive.class);

    private ListAppender<ILoggingEvent> listAppender = new ListAppender<>();

    private FileArchive fileArchiveService = new FileArchive();

    @Test
    @DisplayName("Create zipped file for provided input files.")
    void createZipFile() {

        val actual = this.fileArchiveService.zipFiles(filesToZip, DOWNLOAD_FOLDER, ZIPPED_FILE_NAME);

        assertTrue(actual);

        val zippedFilePath = DOWNLOAD_FOLDER + File.separator + ZIPPED_FILE_NAME;
        assertTrue((new File(zippedFilePath)).exists());
    }

    @Test
    @DisplayName("Will not create zip file if files list is empty")
    void testCreateZipFileDueToEmptyFilesToZip(){
        val actual = this.fileArchiveService.zipFiles(List.of(), DOWNLOAD_FOLDER, ZIPPED_FILE_NAME);

        assertFalse(actual);

        val zippedFilePath = DOWNLOAD_FOLDER + File.separator + ZIPPED_FILE_NAME;
        assertFalse((new File(zippedFilePath)).exists());
    }

    @Test
    @DisplayName("Will not zip non-existent file")
    void willNotCreateZipFileDueToException() {
        listAppender.start();
        logger.addAppender(listAppender);

        this.fileArchiveService.zipFiles(List.of("non-existent-file.txt"), DOWNLOAD_FOLDER, ZIPPED_FILE_NAME);

        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals(
                "Specified file to add to zip was not found (non-existent-file.txt)",
                logsList.get(0).getMessage()
        );
    }

    @Test
    @DisplayName("Will fail on non-existent zip file")
    void willNotCreateZipFileDueToMissingZipFile() {
        listAppender.start();
        logger.addAppender(listAppender);

        this.fileArchiveService.zipFiles(filesToZip, "non-existent-folder", ZIPPED_FILE_NAME);

        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals(
                "Specified zip file was not found (non-existent-folder/download.zip)",
                logsList.get(0).getMessage()
        );
    }

    @BeforeEach
    void setup() {
        filesToZip.forEach(this::createTestFile);
    }

    private void createTestFile(String fileName) {
        try {
            val file = new File(DOWNLOAD_FOLDER + fileName);
            createParentDirs(new File(DOWNLOAD_FOLDER + fileName));
            asCharSink(file, StandardCharsets.UTF_8).write("test content for " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void cleanup() throws IOException {
        FileUtils.deleteDirectory(new File(DOWNLOAD_FOLDER));
    }
}
