package com.droppie.dal.solr.model;

import lombok.*;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.List;

@Data
@Builder
@SolrDocument(collection = "droppie")
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class DropboxFile {
    @Id
    @Indexed(name = "id", type = "string")
    private String id;

    @Field
    private String filename;

    @Indexed(name = "tags", type = "string")
    private List<String> tags;
}
