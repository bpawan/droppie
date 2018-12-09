package com.droppie.dal.solr.repository;

import com.droppie.dal.solr.SolrQueryBuilder;
import com.droppie.dal.solr.model.DropboxFile;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TagsRepository {

    private final SolrTemplate solrTemplate;

    private final SolrQueryBuilder queryBuilder;

    @Autowired
    public TagsRepository(SolrTemplate solrTemplate, SolrQueryBuilder queryBuilder) {
        this.solrTemplate = solrTemplate;
        this.queryBuilder = queryBuilder;
    }

    public Page<DropboxFile> findTagsByQuery(List<String> tags, String operation) {

        if("and".equals(operation.toLowerCase())) {
            tags.forEach(tag -> this.queryBuilder.tag(tag).and());
        } else {
            tags.forEach(tag -> this.queryBuilder.tag(tag).or());
        }

        val queryString = this.queryBuilder.build();

        val query = new SimpleQuery("tags: " + queryString);

        return this.solrTemplate.query("droppie", query, DropboxFile.class);
    }
}
