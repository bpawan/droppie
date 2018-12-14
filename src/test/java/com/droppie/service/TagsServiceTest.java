package com.droppie.service;

import com.droppie.api.model.TaggedFile;
import com.droppie.dal.solr.model.DropboxFile;
import com.droppie.dal.solr.repository.FileRepository;
import com.droppie.service.tag.TagsService;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Tags Service operations (store and delete)")
class TagsServiceTest {

    private FileRepository taggedFileRepository = mock(FileRepository.class);

    private TagsService tagsService = new TagsService(this.taggedFileRepository);

    @Test
    @DisplayName("When file exists on DropBox then repository's store method should called with proper parameters.")
    void willStoreTags() {

        String fileName = "test_file.doc";
        String test_tag1 = "test_tag1";

        val expectedObjectToBeStored = DropboxFile.builder()
                .filename(fileName)
                .tags(List.of(test_tag1))
                .build();

        when(this.taggedFileRepository.save(ArgumentMatchers.eq(expectedObjectToBeStored)))
                .thenReturn(expectedObjectToBeStored);

        val actualResult = this.tagsService.storeOrUpdate(new TaggedFile(fileName, List.of(test_tag1)));

        assertTrue(actualResult.isPresent());
        assertEquals(expectedObjectToBeStored, actualResult.get());
    }

    @Test
    void willDeleteTags() {
        String fileName = "test_file.doc";
        String test_tag1 = "test_tag1";

        val expectedObjectToBeStored = DropboxFile.builder()
                .filename(fileName)
                .tags(List.of(test_tag1))
                .build();
        when(this.taggedFileRepository.save(ArgumentMatchers.eq(expectedObjectToBeStored)))
                .thenReturn(expectedObjectToBeStored);

        this.tagsService.deleteTags(new TaggedFile(fileName, List.of(test_tag1)));
    }
}
