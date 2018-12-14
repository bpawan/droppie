package com.droppie.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.DbxUserFilesRequests;
import com.dropbox.core.v2.files.Metadata;
import com.droppie.service.file.FileLookupService;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("File lookup Service")
class FileLookupServiceTest {

    private final DbxClientV2 dbxClientV2 = mock(DbxClientV2.class);

    private FileLookupService fileLookupService = new FileLookupService(dbxClientV2);

    @Test
    @DisplayName("Assert file exists on server.")
    void fileExistsOnServer() throws Exception {
        val fileName = "test_file.docx";

        val metadataMock = mock(Metadata.class);

        when(metadataMock.getPathLower()).thenReturn(fileName);

        this.stubFileExistsOnDropBox(fileName, metadataMock);

        assertTrue(this.fileLookupService.fileExistsOnServer(fileName));
    }

    @Test
    @DisplayName("Assert file does not exists on server.")
    void fileDoesExistsOnServer() throws Exception {
        val fileName = "test_file.docx";

        val metadataMock = mock(Metadata.class);

        when(metadataMock.getPathLower()).thenReturn("");

        this.stubFileExistsOnDropBox(fileName, metadataMock);

        assertFalse(this.fileLookupService.fileExistsOnServer(fileName));
    }

    @Test
    @DisplayName("Assert exception while requesting file metadata assumes file not be present on server.")
    void fetchingMetadataThrowException() throws Exception {
        val fileName = "test_file.docx";

        val dropBoxClient = mock(DbxClientV2.class);

        val userFilesRequestMock = mock(DbxUserFilesRequests.class);

        when(dropBoxClient.files())
                .thenReturn(userFilesRequestMock);

        when(userFilesRequestMock.getMetadata(fileName)).thenThrow(new DbxException("Oops something went wrong."));

        assertFalse(this.fileLookupService.fileExistsOnServer(fileName));
    }

    private void stubFileExistsOnDropBox(String fileName, Metadata metadata) throws DbxException {
        val dropBoxClient = mock(DbxClientV2.class);
        val userFilesRequestMock = mock(DbxUserFilesRequests.class);

        when(dropBoxClient.files())
                .thenReturn(userFilesRequestMock);

        when(userFilesRequestMock.getMetadata(fileName))
                .thenReturn(metadata);
    }
}
