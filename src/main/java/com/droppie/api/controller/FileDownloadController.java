package com.droppie.api.controller;


import com.droppie.service.FileArchiveService;
import com.droppie.service.FileDownloadService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("files")
public class FileDownloadController {

    private final FileDownloadService fileDownloadService;

    private final FileArchiveService fileArchiveService;

    @Autowired
    public FileDownloadController(FileDownloadService fileDownloadService, FileArchiveService fileArchiveService) {
        this.fileDownloadService = fileDownloadService;
        this.fileArchiveService = fileArchiveService;
    }

    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadFileByTags(@RequestParam(value = "tag") List<String> tags) throws IOException {
        val downloadedFiles = this.fileDownloadService.downloadFilesByTags(tags);

        val zippedFileName = this.fileArchiveService.zipFiles(downloadedFiles, "files/download/", "download.zip");

        val file = new File(zippedFileName);
        Path path = Paths.get(zippedFileName);

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/zip"))
                .body(resource);
    }
}
