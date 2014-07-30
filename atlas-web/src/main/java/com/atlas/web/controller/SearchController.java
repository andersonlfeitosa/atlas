package com.atlas.web.controller;

import com.atlas.core.ejb.SearchServiceEJB;
import com.atlas.core.model.SearchResultVO;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

@Path("/search")
public class SearchController extends AtlasController {
    @Inject
    private SearchServiceEJB searchService;

    @GET
    public String search(@QueryParam("q") String query) {
        List<SearchResultVO> searchResultList = searchService.search(query);

        viewModel.put("searchResults", searchResultList);

        return renderView("search/index");
    }
}
