package com.droppie.service.file;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileLookupService {
    private final DbxClientV2 dbxClientV2;

    @Autowired
    public FileLookupService(DbxClientV2 dbxClientV2) {
        this.dbxClientV2 = dbxClientV2;
    }

    public Boolean fileExistsOnServer(String fileName) {
        try {
            return !dbxClientV2
                    .files()
                    .getMetadata(fileName)
                    .getPathLower()
                    .isEmpty();
        } catch (DbxException e) {
            return false;
        }
    }
}
