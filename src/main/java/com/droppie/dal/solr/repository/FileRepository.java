package com.droppie.dal.solr.repository;

import com.droppie.dal.solr.model.DropboxFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.solr.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends PagingAndSortingRepository<DropboxFile, String> {

    @Query(value = "filename: \"?0\"")
    public DropboxFile findExactByFilename(String filename);

    @Query("tags: \"?0\"")
    public List<DropboxFile> findExactByTag(String query);

    @Query("tags: ?0")
    public Page<DropboxFile> findByTagCombination(String query, Pageable pageable);
}
