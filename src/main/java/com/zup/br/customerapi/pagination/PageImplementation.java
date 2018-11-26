package com.zup.br.customerapi.pagination;

import org.springframework.data.domain.Page;

import java.util.HashMap;

public class PageImplementation {

    private PageImplementation() {        
    }

    public static JSONPage loadPage(Page<?> page, String name) {

        JSONPage jsonPage = new JSONPage();

        HashMap<String, Object> embeddedContent = new HashMap<>();
        HashMap<String, Integer> data = new HashMap<>();

        embeddedContent.put(name, page.getContent());
        jsonPage.set_embedded(embeddedContent);

        data.put("size", page.getSize());
        data.put("totalElements", (int)page.getTotalElements());
        data.put("totalPages", page.getTotalPages());
        data.put("number", page.getNumber());
        jsonPage.setPage(data);

        return jsonPage;
    }

}
