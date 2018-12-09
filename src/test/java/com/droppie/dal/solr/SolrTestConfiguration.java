package com.droppie.dal.solr;

import com.droppie.dal.solr.model.DropboxFile;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.solr.core.SolrTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@SpringBootApplication
public class SolrTestConfiguration {

    public static DropboxFile dropBoxFile1 = DropboxFile.builder()
            .id("unique_id_1")
            .filename("test_file1.docx")
            .tags(List.of("tag2_for_file1", "tag1_for_file1"))
            .build();

    public static DropboxFile dropBoxFile2 = DropboxFile.builder()
            .id("unique_id_2")
            .filename("test_file2.docx")
            .tags(List.of("tag1_for_file2", "tag2_for_file2"))
            .build();

    public static DropboxFile dropBoxFile3 = DropboxFile.builder()
            .id("unique_id_3")
            .filename("test_file3.docx")
            .tags(List.of("tag1_for_file3", "tag2_for_file3"))
            .build();

    public static DropboxFile dropBoxFile4 = DropboxFile.builder()
            .id("unique_id_4")
            .filename("test_file4.docx")
            .tags(List.of("common_tag1", "common_tag2"))
            .build();

    public static DropboxFile dropBoxFile5 = DropboxFile.builder()
            .id("unique_id_5")
            .filename("test_file5.docx")
            .tags(List.of("common_tag1", "common_tag3"))
            .build();

    @Autowired
    PagingAndSortingRepository<DropboxFile, String> repository;

    @Bean
    public SolrTemplate solrTemplate() {
        return new SolrTemplate(new HttpSolrClient.Builder().withBaseSolrUrl("http://localhost:8983/solr").build());
    }

    /**
     * Remove test data when context is shut down.
     */
    @PreDestroy
    public void deleteDocumentsOnShutdown() {
        //repository.deleteAll();
    }

    /**
     * Initialize Solr instance with test data once context has started.
     */
    @PostConstruct
    public void initWithTestData() {
        List.of(dropBoxFile1, dropBoxFile2, dropBoxFile3, dropBoxFile4, dropBoxFile5).forEach(repository::save);

        repository.findAll().forEach(System.out::println);

        System.out.println(repository.findAll());
    }
}
