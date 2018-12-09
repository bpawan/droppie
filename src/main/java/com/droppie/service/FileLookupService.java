package com.droppie.service;

import com.dropbox.core.DbxException;
import com.droppie.dal.drop_box.DropBoxClient;
import lombok.val;
import org.springframework.stereotype.Service;

@Service
public class FileLookupService {
    private final DropBoxClient dropBoxClient;

    public FileLookupService(DropBoxClient dropBoxClient) {
        this.dropBoxClient = dropBoxClient;
    }

    public Boolean fileExistsOnServer(String fileName) {
        val client = this.dropBoxClient.createConnection();
        try {
            return !client
                    .files()
                    .getMetadata(fileName)
                    .getPathLower()
                    .isEmpty();
        } catch (DbxException e) {
            return false;
        }
    }
}
