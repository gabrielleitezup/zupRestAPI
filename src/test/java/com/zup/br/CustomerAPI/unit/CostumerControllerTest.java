package com.zup.br.CustomerAPI.unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zup.br.CustomerAPI.controllers.CustomerController;
import com.zup.br.CustomerAPI.model.City;
import com.zup.br.CustomerAPI.model.Customer;
import com.zup.br.CustomerAPI.repository.CustomerRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CostumerControllerTest {

    @MockBean
    CustomerRepository customerRepository;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testPaginationCustomer() throws Exception{
        Customer customer = new Customer("Gabriel", saveCity());

        List<Customer> customers = Arrays.asList(customer);

        PageImpl page = new PageImpl(customers);

        when(customerRepository.findAll((Pageable) notNull())).thenReturn(page);

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.customers", Matchers.hasSize(1)))
                .andExpect(jsonPath("$._embedded.customers.[0].name", Matchers.is("Gabriel")));
    }

    @Test
    public void testPost() throws Exception {
        String jsonPayload = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("payload/Customer.json"));

        Customer customer = new Customer("Toro", saveCity());

        when(customerRepository.save(notNull())).thenReturn(customer);

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("city.name", Matchers.is("Uberlandia")))
                .andExpect(jsonPath("id", Matchers.notNullValue()));

    }

    @Test
    public void testUpdate() throws Exception {
        Customer edit = new Customer("Jhon", saveCity());
        edit.setId(1);

        Optional<Customer> findByIdResult = Optional.of(edit);

        ObjectMapper objectMapper = new ObjectMapper();

        String jsonContent = objectMapper.writeValueAsString(edit);

        when(customerRepository.findById(edit.getId())).thenReturn(findByIdResult);
        when(customerRepository.save(notNull())).thenReturn(edit);

        mockMvc.perform(put("/customers/" + edit.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonContent)
                .characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", Matchers.is(edit.getId())))
                .andExpect(jsonPath("name", Matchers.is(edit.getName())))
                .andExpect(jsonPath("city.name", Matchers.is(edit.getCity().getName())));

    }

    @Test
    public void testFindById() throws Exception {
        Customer result = new Customer("Gabriel", saveCity());
        result.setId(4);

        Optional<Customer> findByIdResult = Optional.of(result);

        int id = 4;

        when(customerRepository.findById(id)).thenReturn(findByIdResult);

        mockMvc.perform(get("/customers/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", Matchers.is(id)))
                .andExpect(jsonPath("name", Matchers.is("Gabriel")))
                .andExpect(jsonPath("city.name", Matchers.is("Uberlandia")));
    }

    @Test
    public void testFindByName() throws Exception {
        Customer result = new Customer("Gabriel", saveCity());
        String nameFind = result.getName();

        List<Customer> customers = Arrays.asList(result);

        PageImpl findByNameResult = new PageImpl(customers);

        when(customerRepository.findByNameContaining(notNull(), notNull())).thenReturn(findByNameResult);

        mockMvc.perform(get("/customers/search/findByNameIgnoreCaseContaining").param("name", nameFind))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$._embedded.customers", Matchers.hasSize(1)))
                .andExpect(jsonPath("$._embedded.customers.[0].name", Matchers.is("Gabriel")));
    }

    @Test
    public void testDelete() throws Exception{
        Integer id = 4;

        mockMvc.perform(delete("/customers/" + id))
                .andExpect(status().isOk())
                .andDo(print());
    }

    private City saveCity() {
        City city = new City("Uberlandia");
        return city;
    }
}
