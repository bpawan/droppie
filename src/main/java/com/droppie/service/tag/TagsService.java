package com.droppie.service.tag;

import com.droppie.api.model.TaggedFile;
import com.droppie.dal.solr.model.DropboxFile;
import com.droppie.dal.solr.repository.FileRepository;
import lombok.val;
import org.apache.commons.collections4.ListUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TagsService {

    private final FileRepository taggedFileRepository;

    @Autowired
    public TagsService(FileRepository taggedFileRepository) {
        this.taggedFileRepository = taggedFileRepository;
    }

    public Optional<DropboxFile> storeOrUpdate(@NotNull TaggedFile taggedFile) {

        val existingEntry = this.taggedFileRepository.findExactByFilename(taggedFile.getFileName());
        if (existingEntry != null) {
            return updateExistingWith(existingEntry, taggedFile);
        } else {
            return insertEntry(taggedFile);
        }
    }

    public Optional<DropboxFile> insertEntry(@NotNull TaggedFile taggedFile) {
        val entryToInsert = DropboxFile
                .builder()
                .filename(taggedFile.getFileName())
                .tags(taggedFile.getTags())
                .build();

        return Optional.of(this.taggedFileRepository.save(entryToInsert));
    }

    private Optional<DropboxFile> updateExistingWith(DropboxFile existingEntry, TaggedFile taggedFile) {
        this.taggedFileRepository.deleteById(existingEntry.getId());

        val tagsToUpdate = ListUtils
                .union(taggedFile.getTags(), existingEntry.getTags())
                .stream()
                .distinct()
                .collect(Collectors.toList());

        val entryToUpdate = DropboxFile
                .builder()
                .filename(taggedFile.getFileName())
                .tags(tagsToUpdate)
                .id(existingEntry.getId())
                .build();

        return Optional.of(this.taggedFileRepository.save(entryToUpdate));
    }

    public void deleteTags(@NotNull TaggedFile taggedFile) {
        val existingEntry = this.taggedFileRepository.findExactByFilename(taggedFile.getFileName());

        System.out.println(taggedFile);

        System.out.println(existingEntry);

        var updatedTags = taggedFile.getTags();

        if (null != existingEntry.getTags()) {
            updatedTags = existingEntry.getTags()
                    .stream()
                    .filter(existingTag -> taggedFile.getTags().contains(existingTag))
                    .collect(Collectors.toList());
        }

        val modifiedEntry = DropboxFile.builder()
                .filename(taggedFile.getFileName())
                .tags(updatedTags)
                .id(existingEntry.getId())
                .build();

        this.taggedFileRepository.deleteById(existingEntry.getId());

        this.taggedFileRepository.save(modifiedEntry);
    }
}
