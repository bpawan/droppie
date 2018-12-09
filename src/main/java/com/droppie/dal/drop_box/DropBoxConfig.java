package com.droppie.dal.drop_box;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class DropBoxConfig {
    @Value("${dropbox.api.secret}")
    private String secret;
}
