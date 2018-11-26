package com.zup.br.customerapi.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String name;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL)
    private List<Customer> customers;

    public City(@NotBlank String name) {
        this.name = name;
    }

    public City() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
