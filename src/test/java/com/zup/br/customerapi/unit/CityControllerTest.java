package com.zup.br.customerapi.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.br.customerapi.controllers.CityController;
import com.zup.br.customerapi.model.City;
import com.zup.br.customerapi.repository.CityRepository;

import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CityController.class)
public class CityControllerTest {

    private static final String cityTest = "TestCity";

    @MockBean
    CityRepository cityRepository;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testPaginationCity() throws Exception {
        City city = new City("Uber");
        List<City> cities = Arrays.asList(city);

        PageImpl page = new PageImpl(cities);

        when(cityRepository.findAll((Pageable) notNull())).thenReturn(page);

        mockMvc.perform(get("/cities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.cities", Matchers.hasSize(1)))
                .andExpect(jsonPath("$._embedded.cities.[0].name", Matchers.is("Uber")));
    }

    @Test
    public void testPost() throws Exception {
        String jsonPayload = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("payload/City.json"));

        City city = new City("Rerigueri");

        when(cityRepository.save(notNull())).thenReturn(city);

        mockMvc.perform(post("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id", Matchers.notNullValue()));

    }

    @Test
    public void testUpdate() throws Exception {
        City edit = new City("Araxa");
        edit.setId(1);

        Optional<City> findByIdResult = Optional.of(edit);

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonContent = objectMapper.writeValueAsString(edit);

        when(cityRepository.findById(edit.getId())).thenReturn(findByIdResult);
        when(cityRepository.save(notNull())).thenReturn(edit);

        mockMvc.perform(put("/cities/" + edit.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", Matchers.is((int)edit.getId())))
                .andExpect(jsonPath("name", Matchers.is(edit.getName())));

    }

    @Test
    public void testFindById() throws Exception {
        City result = new City(cityTest);
        result.setId(4);

        Optional<City> findByIdResult = Optional.of(result);

        long id = 4;

        when(cityRepository.findById(id)).thenReturn(findByIdResult);

        mockMvc.perform(get("/cities/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", Matchers.is((int)id)))
                .andExpect(jsonPath("name", Matchers.is(cityTest)));
    }

    @Test
    public void testFindByName() throws Exception {
        City result = new City(cityTest);
        List<City> search = Arrays.asList(result);
        String nameFind = cityTest;

        PageImpl page = new PageImpl(search);

        when(cityRepository.findByNameContaining(notNull(), notNull())).thenReturn(page);

        mockMvc.perform(get("/cities/search/findByNameIgnoreCaseContaining").param("name", nameFind))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.cities", Matchers.hasSize(1)))
                .andExpect(jsonPath("$._embedded.cities.[0].name", Matchers.is(nameFind)));

    }

    @Test
    public void testDelete() throws Exception {
        Integer id = 4;

        mockMvc.perform(delete("/cities/" + id))
                .andExpect(status().isOk())
                .andDo(print());
    }

}
