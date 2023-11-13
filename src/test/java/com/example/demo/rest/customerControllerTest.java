package com.example.demo.rest;

import com.example.demo.dto.KafkaMessageDTO;
import com.example.demo.entity.Customer;
import com.example.demo.kafka.KafkaProducerService;
import com.example.demo.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class customerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KafkaProducerService kafkaProducerService;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    public void testCustomersJwt() throws Exception {
        Customer customer1 = new Customer();
        customer1.setAddress("1111 foo blvd");
        customer1.setName("Foo Industries");
        customer1.setServiceRendered("Important services");

        Customer customer2 = new Customer();
        customer2.setAddress("2222 bar street");
        customer2.setName("Bar LLP");
        customer2.setServiceRendered("Important services");

        // stub the customerRepository to return the mock customers
        when(customerRepository.findAll()).thenReturn(List.of(customer1, customer2));

        // perform a GET request to the customers page with the mock principal
        mockMvc.perform(get("/customers-jwt").with(jwt().authorities(List.of(
                                new SimpleGrantedAuthority("ROLE_USER")))
                        .jwt(jwt -> jwt.claim(StandardClaimNames.PREFERRED_USERNAME, "user1"))))
                .andExpect(status().isOk())
                .andExpect(view().name("customers"));
    }


    @Test
    public void testProduceKafkaMessage() throws Exception {
        // create a mock message
        KafkaMessageDTO msg = new KafkaMessageDTO();
        msg.setKey("key");
        msg.setValue("value");

        // perform a POST request to the kafka-produce endpoint with the mock message
        mockMvc.perform(post("/kafka-produce")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(msg)).with(jwt().authorities(List.of(
                                        new SimpleGrantedAuthority("ROLE_USER")))
                                .jwt(jwt -> jwt.claim(StandardClaimNames.PREFERRED_USERNAME, "user1"))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(kafkaProducerService, times(1)).produce(msg);
    }

    // helper method to convert an object to a JSON string
    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}