package com.droppie.dal.drop_box;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import lombok.val;
import org.springframework.stereotype.Component;

@Component
public class DropBoxClient {

    private final DropBoxConfig dropBoxConfig;

    private DbxClientV2 dbxClientV2 = null;

    public DropBoxClient(DropBoxConfig dropBoxConfig) {
        this.dropBoxConfig = dropBoxConfig;
    }

    public DbxClientV2 createConnection() {
        val config = DbxRequestConfig.newBuilder("droppie").build();

        if(null == this.dbxClientV2) {
            this.dbxClientV2 = new DbxClientV2(config, this.dropBoxConfig.getSecret());
        }

        return this.dbxClientV2;
    }
}
