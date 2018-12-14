package com.droppie.service.file;

import com.dropbox.core.v2.files.FileMetadata;
import com.droppie.dal.solr.model.DropboxFile;
import com.droppie.dal.solr.repository.FileRepository;
import com.droppie.service.exception.CannotCreateDownloadFolderException;
import com.droppie.service.exception.FileDownloadException;
import com.droppie.service.model.FileDownloadStats;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FileDownloadService {

    private static final String DOWNLOAD_FOLDER = "files/download/";

    private final FileRepository taggedFileRepository;

    private final FileDownloader fileDownloader;

    @Autowired
    public FileDownloadService(FileRepository taggedFileRepository, FileDownloader fileDownloader) {
        this.taggedFileRepository = taggedFileRepository;
        this.fileDownloader = fileDownloader;
    }


    public List<FileDownloadStats> downloadFilesByTags(List<String> tags) throws FileDownloadException {

        if (!new File(DOWNLOAD_FOLDER).mkdirs()) {
            throw new CannotCreateDownloadFolderException();
        }

        return tags.stream()
                .map((tag) -> {
                    val filesByTag = this.taggedFileRepository.findExactByTag(tag);

                    return new FileDownloadStats(tag, downloadFiles(filesByTag));
                })
                .collect(Collectors.toList());
    }

    @NotNull
    private List<FileMetadata> downloadFiles(List<DropboxFile> filesByTag) {
        List<FileMetadata> downloadedFiles = new ArrayList<>();

        filesByTag.forEach(taggedFile -> {
            val fileName = this.createFileName(taggedFile.getFilename());

            this.fileDownloader.downloadFile(fileName, "");

            //optionalFileMetadata.ifPresent(downloadedFiles::add);
        });

        return downloadedFiles;
    }


    private String createFileName(String filename) {
        return Paths.get(filename).getFileName().toString();
    }

}
