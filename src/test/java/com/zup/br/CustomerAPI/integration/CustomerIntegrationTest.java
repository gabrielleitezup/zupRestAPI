package com.zup.br.CustomerAPI.integration;

import com.zup.br.CustomerAPI.model.City;
import com.zup.br.CustomerAPI.model.Customer;
import com.zup.br.CustomerAPI.repository.CityRepository;
import com.zup.br.CustomerAPI.repository.CustomerRepository;
import net.minidev.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class CustomerIntegrationTest {

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    WebApplicationContext context;

    MockMvc mockMvc;

    @Before
    public void initializer() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void testFindAllCustomers() throws Exception {
        saveAllCustomers();

        this.mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testCreateACustomer() throws Exception {
        String jsonContent = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("payload/Customer.json"));

        this.mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void testUpdateACustomer() throws Exception {
        Customer test = saveACustomer();
        City cityTest = saveCity();
        int id = test.getId();

        Map<String, Object> data = new HashMap<>();
        Map<String, Integer> dataCity = new HashMap<>();
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
    public void testFindCustomerById() throws Exception {
        Customer test = saveACustomer();
        int id = test.getId();
        String name = test.getNameCustomer();

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
