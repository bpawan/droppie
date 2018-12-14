package com.droppie.dal.solr;

import com.droppie.dal.solr.repository.FileRepository;
import com.droppie.utils.RequiresSolrServer;
import lombok.val;
import org.junit.ClassRule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.query.SimpleQuery;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@SpringBootTest(classes = SolrTestConfiguration.class)
@DisplayName("Integration tests for TaggedFileRepository class")
public class TaggedFileRepositoryTest {
    @ClassRule
    public static RequiresSolrServer requiresSolrServer = RequiresSolrServer.onLocalhost();

    @Autowired
    private FileRepository fileRepository;

    @Test
    @DisplayName("findExactByFilename will returns exactly one matching entry otherwise null.")
    void findExactByFilename() {
        assertEquals(
                fileRepository.findExactByFilename("test_file1.docx"),
                SolrTestConfiguration.dropBoxFile1
        );

        assertNull(fileRepository.findExactByFilename("test_file.docx"));
        assertNull(fileRepository.findExactByFilename("test_file.docx1"));
        assertNull(fileRepository.findExactByFilename("test"));
        assertNull(fileRepository.findExactByFilename("file"));
    }

    @Test
    @DisplayName("findExactByTag will return entries matching the exact tag.")
    void deleteByFilename() {
        val actual = this.fileRepository.findExactByTag(SolrTestConfiguration.dropBoxFile1.getTags().get(0));

        assertEquals(1, actual.size());
        assertEquals(SolrTestConfiguration.dropBoxFile1, actual.get(0));

        assertEquals(0, fileRepository.findExactByTag("test").size());
    }

    @Test
    @DisplayName("findByTagConjunctionWithPagination will return entries with AND conjunction")
    void andConjunction() {

        val queryBuilder = new SolrQueryBuilder();

        val tags = List.of("tag2_for_file1", "tag1_for_file1");

        val queryString = queryBuilder.tag("tag2_for_file1").and().tag("tag1_for_file1").build();


        val query = new SimpleQuery(queryString);

        val pap = this.fileRepository.findByTagCombination(queryString, PageRequest.of(1, 1));


        //val pap = this.fileRepository.findByQuery(tags, PageRequest.of(1, 1));

        System.out.println(pap.getContent());


        //val actual = this.tagsRepository.findTagsByQuery(List.of("tag2_for_file1", "tag1_for_file1"), "and");

        //System.out.println(actual);

        //assertEquals(actual, pap);


        //System.out.println(actual.getContent());

        //assertEquals(1, pap.getTotalPages());
        //assertEquals(1, pap.getNumberOfElements());
    }

    @Test
    @DisplayName("findByTagConjunctionWithPagination will return entries with OR conjunction")
    void orConjunction() {

    }
}
