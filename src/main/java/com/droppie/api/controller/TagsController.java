package com.droppie.api.controller;

import com.droppie.api.exception.UnableToStoreTagsException;
import com.droppie.api.model.TaggedFile;
import com.droppie.dal.solr.model.DropboxFile;
import com.droppie.service.FileLookupService;
import com.droppie.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("tags")
public class TagsController {

    private final TagsService fileTaggingService;

    private final FileLookupService fileLookupService;

    @Autowired
    public TagsController(TagsService fileTaggingService, FileLookupService fileLookupService) {
        this.fileTaggingService = fileTaggingService;
        this.fileLookupService = fileLookupService;
    }

    @PostMapping
    public DropboxFile addTagsToFile(@Valid  @RequestBody TaggedFile taggedFile) throws UnableToStoreTagsException {

        if(this.fileLookupService.fileExistsOnServer(taggedFile.getFileName())) {
            return this.fileTaggingService
                    .storeOrUpdate(taggedFile)
                    .orElseThrow(UnableToStoreTagsException::new);
        } else {
            throw new UnableToStoreTagsException();
        }
    }

    @DeleteMapping
    public void deleteTags(@Valid @RequestBody TaggedFile taggedFile) {
        if(this.fileLookupService.fileExistsOnServer(taggedFile.getFileName())) {
            this.fileTaggingService.deleteTags(taggedFile);
        }
    }
}
