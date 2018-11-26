package com.zup.br.customerapi.pagination;

import java.util.Map;

public class JSONPage {

    private Map<String, Object> _embedded;
    private Map<String, Integer> page;

    public Map<String, Object> get_embedded() {
        return _embedded;
    }

    public void set_embedded(Map<String, Object> _embedded) {
        this._embedded = _embedded;
    }

    public Map<String, Integer> getPage() {
        return page;
    }

    public void setPage(Map<String, Integer> page) {
        this.page = page;
    }
}
