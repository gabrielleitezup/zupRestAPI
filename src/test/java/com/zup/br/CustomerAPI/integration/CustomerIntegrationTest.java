package com.zup.br.CustomerAPI.integration;

import com.zup.br.CustomerAPI.AbstractTest;
import com.zup.br.CustomerAPI.model.City;
import com.zup.br.CustomerAPI.model.Customer;

import net.minidev.json.JSONObject;

import org.hamcrest.Matchers;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CustomerIntegrationTest extends AbstractTest {

    @Test
    public void testFindAllCustomers() throws Exception {
        saveAllCustomers();

        this.mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testCreateACustomer() throws Exception {
        long id = saveCity().getId();

        Map<String, Object> data = new HashMap<>();
        Map<String, Long> dataCity = new HashMap<>();
        dataCity.put("id", id);
        data.put("name", "Juliel");
        data.put("city", dataCity);

        this.mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject.toJSONString(data)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void testCreateEmptyCustomer() throws Exception {
        long id = saveCity().getId();

        Map<String, Object> data = new HashMap<>();
        Map<String, Long> dataCity = new HashMap<>();
        dataCity.put("id", id);
        data.put("name", "");
        data.put("city", dataCity);

        String errorMessage = "name:must not be blank";

        this.mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject.toJSONString(data)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("message", Matchers.is(errorMessage)))
                .andDo(print());
    }

    @Test
    public void testUpdateACustomer() throws Exception {
        Customer test = saveACustomer();
        City cityTest = saveCity();
        int id = test.getId();

        Map<String, Object> data = new HashMap<>();
        Map<String, Long> dataCity = new HashMap<>();
        dataCity.put("id", cityTest.getId());
        data.put("name", "Juliel");
        data.put("city", cityTest);


        this.mockMvc.perform(put("/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject.toJSONString(data))
                .characterEncoding("utf-8"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testUpdateACustomerWithoutId() throws Exception {
        City cityTest = saveCity();
        Random rand = new Random();
        int id = rand.nextInt(50);

        Map<String, Object> data = new HashMap<>();
        Map<String, Long> dataCity = new HashMap<>();
        dataCity.put("id", cityTest.getId());
        data.put("name", "Juliel");
        data.put("city", cityTest);


        this.mockMvc.perform(put("/customers/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JSONObject.toJSONString(data))
                .characterEncoding("utf-8"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testFindCustomerById() throws Exception {
        Customer test = saveACustomer();
        int id = test.getId();
        String name = test.getName();

        this.mockMvc.perform(get("/customers/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", Matchers.is(id)))
                .andExpect(jsonPath("name", Matchers.is(name)))
                .andDo(print());
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        Customer test = saveACustomer();
        int id = test.getId();

        this.mockMvc.perform(delete("/customers/" + id))
                .andExpect(status().isOk())
                .andDo(print());
    }

    private Customer saveACustomer() {
        Customer customer = new Customer("BIRL", saveCity());
        return customerRepository.save(customer);
    }

    private void saveAllCustomers() {
        Customer customer1 = new Customer("Gabriel", saveCity());
        customerRepository.save(customer1);

        Customer customer2 = new Customer("Bruno", saveCity());
        customerRepository.save(customer2);

        Customer customer3 = new Customer("Murilo", saveCity());
        customerRepository.save(customer3);

        Customer customer4 = new Customer("Vinicius", saveCity());
        customerRepository.save(customer4);
    }

    private City saveCity() {
        City city = new City("Uberlandia");
        return cityRepository.save(city);
    }

}
