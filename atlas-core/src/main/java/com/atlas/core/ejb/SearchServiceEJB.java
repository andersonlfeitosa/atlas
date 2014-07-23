package com.atlas.core.ejb;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class SearchServiceEJB {

    public void initialize() {
        System.out.println("Init...");
    }
}
