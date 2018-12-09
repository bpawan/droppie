package com.droppie.service;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.droppie.dal.drop_box.DropBoxClient;
import com.droppie.dal.solr.model.DropboxFile;
import com.droppie.dal.solr.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileDownloadService {

    public static final String DOWNLOAD_FOLDER = "files/download/";

    private final FileRepository taggedFileRepository;

    private final DropBoxClient dropBoxClient;

    private List<String> downloadedFiles = new ArrayList<>();

    @Autowired
    public FileDownloadService(FileRepository taggedFileRepository, DropBoxClient dropBoxClient) {
        this.taggedFileRepository = taggedFileRepository;
        this.dropBoxClient = dropBoxClient;
    }

    public List<String> downloadFilesByTags(List<String> tags) {
        val dropBoxConnection = this.dropBoxClient.createConnection();

        new File(DOWNLOAD_FOLDER).mkdirs();

        return tags
                .stream()
                .flatMap((String tag) -> downloadFiles(dropBoxConnection, this.taggedFileRepository.findExactByTag(tag)))
                .distinct()
                .collect(Collectors.toList());
    }

    private Stream<String> downloadFiles(DbxClientV2 dropBoxConnection, List<DropboxFile> taggedFiles) {
        taggedFiles.forEach(taggedFile -> {
            val fileName = this.createFileName(taggedFile.getFilename());
            try {
                if (!this.downloadedFiles.contains(taggedFile.getFilename())) {

                    val fileToDownload = dropBoxConnection.files().getMetadata(taggedFile.getFilename()).getPathLower();
                    val downloader = dropBoxConnection.files().download(taggedFile.getFilename());
                    val fileOutputStream = new FileOutputStream(DOWNLOAD_FOLDER.concat(fileName));
                    downloader.download(fileOutputStream);

                    this.downloadedFiles.add(fileToDownload);
                }
            } catch (DbxException | IOException e) {
                log.error(String.format("Problem downloading %s, %s", fileName, Arrays.toString(e.getStackTrace())));
            }
        });

        return this.downloadedFiles.stream();
    }

    private String createFileName(String filename) {
        return Paths.get(filename).getFileName().toString();
    }
}
