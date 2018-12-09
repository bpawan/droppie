package com.droppie.api.controller;

import com.droppie.api.model.SearchByTagRequestBody;
import com.droppie.dal.solr.model.DropboxFile;
import com.droppie.service.PaginatingFileSearchService;
import com.droppie.service.exception.UnsupportedSearchOperationException;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.UriTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "files")
public class FileSearchController {

    private final PaginatingFileSearchService fileService;

    @Autowired
    public FileSearchController(PaginatingFileSearchService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/search")
    public HttpEntity<Resources> loadByTags(
            @RequestParam(value = "tag") List<String> tags,
            @RequestParam(value = "operator", required = false, defaultValue = "OR") String operator,
            Pageable pageable,
            PagedResourcesAssembler<DropboxFile> assembler
    ) throws UnsupportedSearchOperationException {

        val pagedDropBoxFiles = this.fileService.findFilesByTags(tags, operator, pageable);

        Link selfLink = createPaginationLinks(new SearchByTagRequestBody(tags, operator), pageable, assembler, pagedDropBoxFiles);

        val resources = assembler.toResource(pagedDropBoxFiles);
        resources.removeLinks();
        resources.add(selfLink);

        return new ResponseEntity<>(resources, HttpStatus.OK);
    }

    private Link createPaginationLinks(
            SearchByTagRequestBody searchByTagRequestBody,
            Pageable pageable,
            PagedResourcesAssembler<DropboxFile> assembler,
            Page<DropboxFile> pagedDropBoxFiles
    ) throws UnsupportedSearchOperationException {

        val pageNumber = pagedDropBoxFiles.getPageable().getPageNumber();
        val pageSize = pagedDropBoxFiles.getPageable().getPageSize();

        val controllerLinkBuilder = linkTo(methodOn(FileSearchController.class).loadByTags(searchByTagRequestBody.getTags(), searchByTagRequestBody.getOperator(), pageable, assembler));

        val uriComponentBuilder = controllerLinkBuilder.toUriComponentsBuilder();

        uriComponentBuilder.replaceQueryParam("page", pageNumber);
        uriComponentBuilder.replaceQueryParam("size", pageSize);

        return new Link(new UriTemplate(uriComponentBuilder.build().toString()), "self");
    }
}
