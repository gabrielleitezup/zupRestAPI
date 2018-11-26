package com.zup.br.CustomerAPI.integration;

import com.zup.br.CustomerAPI.AbstractTest;
import com.zup.br.CustomerAPI.model.City;

import net.minidev.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CityIntegrationTest extends AbstractTest {

    private final String PATH = "/cities/";
    private final String CHTR_ENCOND = "utf-8";

    @Test
    public void testFindAllCities() throws Exception {
        saveAllCities();

        this.mockMvc.perform(get("/cities"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testCreateCity() throws Exception {
        String jsonContent = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("payload/City.json"));
        this.mockMvc.perform(post("/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void testUpdateCity() throws Exception {
        City test = saveACity();
        int id = Integer.parseInt(valueOf(test.getId()));

        Map<String, String> data = new HashMap<>();
        data.put("name", "Aden");

        this.mockMvc.perform(put(PATH + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject.toJSONString(data))
                .characterEncoding(CHTR_ENCOND))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", Matchers.is(id)))
                .andDo(print());
    }

    @Test
    public void testUpdateCityWithRandomId() throws Exception {
        City test = saveACity();
        int id = Integer.parseInt(valueOf(test.getId()));

        Map<String, String> data = new HashMap<>();
        data.put("id", valueOf(121));
        data.put("name", "Aden");

        this.mockMvc.perform(put(PATH + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject.toJSONString(data))
                .characterEncoding(CHTR_ENCOND))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", Matchers.is(id)))
                .andDo(print());
    }

    @Test
    public void testUpdateCityWithIdNonExistent() throws Exception {
        int id = 114;

        Map<String, String> data = new HashMap<>();
        data.put("name", "Aden");

        this.mockMvc.perform(put(PATH + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject.toJSONString(data))
                .characterEncoding(CHTR_ENCOND))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testFindCityById() throws Exception {
        City result = saveACity();
        long id = result.getId();
        String name = result.getName();

        this.mockMvc.perform(get(PATH + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", Matchers.is((int)id)))
                .andExpect(jsonPath("name", Matchers.is(name)));
    }

    @Test
    public void testDeleteCity() throws Exception{
        City deleted = saveACity();
        assertThat(deleted.getId()).isNotNull();

        long id = deleted.getId();

        this.mockMvc.perform(delete(PATH + id))
                .andExpect(status().isOk())
                .andDo(print());
    }

    private City saveACity() {
        City city1 = new City("Burg");
        return cityRepository.save(city1);
    }

    private void saveAllCities() {
        City city1 = new City("Jeda");
        cityRepository.save(city1);

        City city2 = new City("Tatooine");
        cityRepository.save(city2);

        City city3 = new City("Nifflhein");
        cityRepository.save(city3);

        City city4 = new City("Outerworld");
        cityRepository.save(city4);
    }

}
