package com.shell.core.services;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.jcr.Session;
import java.util.List;
import java.util.Map;

@Component(service = SearchService.class)
public class SearchService {

    @Reference
    private QueryBuilder queryBuilder;

    /**
     * Execute a search query with the given predicates under the given session.
     */
    public SearchResult search(Map<String, String> predicates, Session session) {
        Query query = queryBuilder.createQuery(PredicateGroup.create(predicates), session);
        return query.getResult();
    }

    /**
     * Helper method to extract hits from SearchResult.
     */
    public List<Hit> getHits(SearchResult searchResult) {
        return searchResult.getHits();
    }
}
