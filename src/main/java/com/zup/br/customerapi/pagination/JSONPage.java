package com.zup.br.customerapi.pagination;

import java.util.HashMap;
import java.util.Map;

public class JSONPage {

    private HashMap<String, Object> _embedded;
    private HashMap<String, Integer> page;

    public Map<String, Object> get_embedded() {
        return _embedded;
    }

    public void set_embedded(HashMap<String, Object> _embedded) {
        this._embedded = _embedded;
    }

    public Map<String, Integer> getPage() {
        return page;
    }

    public void setPage(HashMap<String, Integer> page) {
        this.page = page;
    }
}
