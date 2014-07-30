package com.atlas.core.ejb;

import com.atlas.core.model.SearchResultVO;
import com.atlas.core.util.Constants;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Stateless
@LocalBean
public class SearchServiceEJB {
    private static final Logger logger = LoggerFactory.getLogger(SearchServiceEJB.class);
    private static ObjectMapper objectMapper;

    @Inject
    private IndexServiceEJB indexService;

    public SearchServiceEJB() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    }

    public List<SearchResultVO> search(String query) {
        Client client = indexService.getNode().client();

        // TODO choose the best options from
        // http://www.elasticsearch.org/guide/en/elasticsearch/client/java-api/current/query-dsl-queries.html
        SearchResponse searchResponse = client.prepareSearch(Constants.ES_INDEX_NAME)
                .setTypes(Constants.ES_SVN_ITEM_TYPE_NAME)
                .setQuery(QueryBuilders.queryString(query))
                // .setFrom(0).setSize(100).setExplain(true)
                .execute()
                .actionGet();

        List<SearchResultVO> searchResultList = new ArrayList<>();

        try {
            for (SearchHit hit : searchResponse.getHits()) {
                SearchResultVO searchResultVO = objectMapper.readValue(hit.getSourceAsString(), SearchResultVO.class);
                searchResultList.add(searchResultVO);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        logger.info("Search for '" + query + "'. [" + searchResponse.getHits().getTotalHits() + "]");

        return searchResultList;
    }
}
