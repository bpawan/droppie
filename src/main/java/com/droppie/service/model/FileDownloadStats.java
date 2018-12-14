package com.droppie.service.model;

import com.dropbox.core.v2.files.FileMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FileDownloadStats {
    private String tag;

    private List<FileMetadata> stats;
}
