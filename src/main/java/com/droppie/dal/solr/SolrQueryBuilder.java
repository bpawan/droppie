package com.droppie.dal.solr;

import org.springframework.stereotype.Component;

@Component
public class SolrQueryBuilder {

    private String query = "";

    public SolrQueryBuilder tag(String tag) {
        this.query += tag + " ";

        return this;
    }

    public SolrQueryBuilder and() {
        this.query += "and";
        return this;
    }

    public SolrQueryBuilder or() {
        this.query += "or";
        return this;
    }

    public String build() {
        if (!this.query.isEmpty() || !this.query.endsWith("AND") || !this.query.endsWith("OR")) {
            return this.query;
        }

        return this.query;
    }
}
