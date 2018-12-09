package com.droppie.service;

import com.droppie.dal.drop_box.DropBoxClient;
import com.droppie.dal.solr.SolrQueryBuilder;
import com.droppie.dal.solr.model.DropboxFile;
import com.droppie.dal.solr.repository.FileRepository;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaginatingFileSearchService {

    private final FileRepository taggedFileRepository;

    private final DropBoxClient dropBoxClient;

    @Autowired
    public PaginatingFileSearchService(FileRepository taggedFileRepository, DropBoxClient dropBoxClient) {
        this.taggedFileRepository = taggedFileRepository;
        this.dropBoxClient = dropBoxClient;
    }

    public Page<DropboxFile> findFilesByTags(List<String> tags, String operator, Pageable pageable) {

        //val filesByTags = this.taggedFileRepository
          //      .findByTagConjunctionWithPagination(buildQuery(tags, operator), pageable);

        //filesByTags.filter(file -> this.fileDoesNotExists(file.getFilename()));

        return  null;
    }

    private String buildQuery(List<String> tags, String operator) {
        val queryBuilder = new SolrQueryBuilder();

        tags.forEach(tag -> {
            if (operator.toLowerCase().equals("and")) {
                queryBuilder.tag(tag).and();
            } else {
                queryBuilder.tag(tag).or();
            }
        });

        return queryBuilder.build();
    }

    private Boolean fileDoesNotExists(String fileName) {
        val client = this.dropBoxClient.createConnection();
        try {
            return client
                    .files()
                    .getMetadata(fileName)
                    .getPathLower()
                    .isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
}
