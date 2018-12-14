package com.droppie.service;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DbxUserFilesRequests;
import com.dropbox.core.v2.files.FileMetadata;
import com.droppie.service.file.FileDownloader;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Test File Downloader")
class FileDownloaderTest {


    @Test
    @DisplayName("Downloading file will succeed.")
    void downloadFile() throws Exception {
        val dropBoxFilePath = "file.txt";
        val downloadLocation = "files/";

        val fileOutputStream = new FileOutputStream(downloadLocation.concat(dropBoxFilePath));

        val mockedFiles = mock(DbxUserFilesRequests.class);


        DbxDownloader mockedDbxDownloader = mock(DbxDownloader.class);

        when(mockedFiles.download(dropBoxFilePath + File.separator + dropBoxFilePath))
                .thenReturn(mockedDbxDownloader);

        when(mockedDbxDownloader.download(fileOutputStream)).thenReturn(mock(FileMetadata.class));

        val dbxClientV2 = mock(DbxClientV2.class);

        when(dbxClientV2.files()).thenReturn(mockedFiles);


        val fileDownloader = new FileDownloader(dbxClientV2);

        fileDownloader.downloadFile(dropBoxFilePath, downloadLocation).subscribe(System.out::println);

        //val expectedFileMeta = new FileMetadata();

        /*StepVerifier
                .create(this.fileDownloader.downloadFile(dropBoxFilePath, downloadLocation))
                .expectNext(expectedFileMeta)
                .verifyComplete();
                */
    }
}
