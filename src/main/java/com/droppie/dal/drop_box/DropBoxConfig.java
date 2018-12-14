package com.droppie.dal.drop_box;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DropBoxConfig {
    @Value("${dropbox.api.secret}")
    private String secret;

    @Value("${dropbox.client.identifier}")
    private String clientIdentifier;

    @Bean
    DbxClientV2 dbxClientV2() {
        return new DbxClientV2(DbxRequestConfig.newBuilder(clientIdentifier).build(),this.secret);
    }
}
