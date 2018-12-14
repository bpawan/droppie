package com.droppie.api.controller;


import com.droppie.service.file.FileDownloadService;
import com.droppie.util.file.FileArchive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("files")
public class FileDownloadController {

    private final FileDownloadService fileDownloadService;

    private final FileArchive fileArchiveService;

    @Autowired
    public FileDownloadController(FileDownloadService fileDownloadService, FileArchive fileArchiveService) {
        this.fileDownloadService = fileDownloadService;
        this.fileArchiveService = fileArchiveService;
    }

    /*
    @GetMapping("/download")
    public ResponseEntity<ByteArrayResource> downloadFileByTags(@RequestParam(value = "tag") List<String> tags) throws IOException {
        val downloadedFiles = this.fileDownloadService.downloadFilesByTags(tags);

        this.fileArchiveService.zipFiles(downloadedFiles, "files/download/", "download.zip");

        val file = new File("files/download/download.zip");
        Path path = Paths.get("files/download/download.zip");

        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/zip"))
                .body(resource);
    }
    */
}
