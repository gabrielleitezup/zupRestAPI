package com.zup.br.customerapi.pagination;

import org.springframework.data.domain.Page;

import java.util.HashMap;

public class PageImplementation {

    public static JSONPage loadPage(Page<?> page, String name) {

        JSONPage jsonPage = new JSONPage();

        HashMap<String, Object> _embeddedContent = new HashMap<>();
        HashMap<String, Integer> data = new HashMap<>();

        _embeddedContent.put(name, page.getContent());
        jsonPage.set_embedded(_embeddedContent);

        data.put("size", page.getSize());
        data.put("totalElements", (int)page.getTotalElements());
        data.put("totalPages", page.getTotalPages());
        data.put("number", page.getNumber());
        jsonPage.setPage(data);

        return jsonPage;
    }

}
