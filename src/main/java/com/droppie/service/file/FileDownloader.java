package com.droppie.service.file;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.lang.String.format;

@Slf4j
@Component
public class FileDownloader {

    private final DbxClientV2 dbxClientV2;

    @Autowired
    public FileDownloader(DbxClientV2 dbxClientV2) {
        this.dbxClientV2 = dbxClientV2;
    }

    /**
     * This function will download the file form the dropbox server.
     *
     * @param dropBoxFilePath The path of the file on dropbox server.
     * @param downloadLocation The location where to store the file on local server.
     *
     * @return The {@link Mono} of {@link FileMetadata}.
     */
    public Mono<FileMetadata> downloadFile(@NonNull String dropBoxFilePath, @NonNull String downloadLocation) {

        return this
                .createDbxDownloader(dropBoxFilePath)
                .flatMap(downloader -> download(createDownloadPath(dropBoxFilePath, downloadLocation), downloader));
    }

    /**
     * Creates the file download path for the local server.
     *
     * @param dropBoxFilePath The path of the file on dropbox server.
     * @param downloadLocation The location where to store the file on local server.
     *
     * @return Returns the {@link String} of complete path of the file to store in file system.
     */
    private String createDownloadPath(String dropBoxFilePath, String downloadLocation) {
        return downloadLocation.concat(File.separator).concat(dropBoxFilePath);
    }

    /**
     * Carry out the operation to download the file from the server.
     * The actual operation of downloading file and storing both are blocking operations,
     * so it uses separate threads to do so.
     *
     * @param outputFile The complete file path with the filename to store the file on file system.
     * @param dbxDownloader The downloader to download the file.
     *
     * @return Returns the {@link Mono} of {@link FileMetadata} or MonoEmpty if failed.
     */
    private Mono<FileMetadata> download(String outputFile, DbxDownloader<FileMetadata> dbxDownloader) {
        try {
            return Mono
                    .just(dbxDownloader.download(new FileOutputStream(outputFile)))
                    .subscribeOn(Schedulers.elastic())
                    .doFinally(signalType -> Schedulers.shutdownNow());
        } catch (DbxException exception) {
            log.error(format("Unreadable response received for file (%s)", outputFile));
        } catch (IOException e) {
            log.error(format("Failed creating file to store downloaded content for file (%s)", outputFile));
        }

        return Mono.empty();
    }

    /**
     * This function will create the downloader based on the path provided it to it. As well it will also
     * considers the client context provided with the configuration {@link com.droppie.dal.drop_box.DropBoxConfig}
     *
     * @param dropBoxFilePath The path of the file on dropbox.
     *
     * @return Will return {@link Mono} {@link DbxDownloader} of {@link FileMetadata} type when succeds.
     * Otherwise return empty mono.
     *
     */
    private Mono<DbxDownloader<FileMetadata>> createDbxDownloader(String dropBoxFilePath) {

        try {
            return Mono
                    .just(this.dbxClientV2.files().download(dropBoxFilePath))
                    .subscribeOn(Schedulers.elastic())
                    .doFinally(signalType -> Schedulers.shutdownNow());
        } catch (DbxException e1) {
            log.error(format("Problem loading the metadata for file (%s)", dropBoxFilePath));

            return Mono.empty();
        }
    }
}
