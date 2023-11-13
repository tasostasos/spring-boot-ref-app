package com.example.demo.entity;

import com.example.demo.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class CustomerTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void testCreateCustomer() {
        // Given
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setServiceRendered("Plumbing");
        customer.setAddress("123 Main Street");

        // When
        Customer savedCustomer = customerRepository.save(customer);

        // Then
        assertNotNull(savedCustomer.getId());
        assertEquals("John Doe", savedCustomer.getName());
        assertEquals("Plumbing", savedCustomer.getServiceRendered());
        assertEquals("123 Main Street", savedCustomer.getAddress());
    }

    @Test
    public void testFindAllCustomers() {
        // Given
        Customer customer1 = new Customer();
        customer1.setName("John Doe");
        customer1.setServiceRendered("Plumbing");
        customer1.setAddress("123 Main Street");

        Customer customer2 = new Customer();
        customer2.setName("Jane Doe");
        customer2.setServiceRendered("Cleaning");
        customer2.setAddress("456 Main Street");

        customerRepository.save(customer1);
        customerRepository.save(customer2);

        // When
        List<Customer> customers = (List<Customer>) customerRepository.findAll();

        // Then
        assertEquals(2, customers.size());
        assertEquals("John Doe", customers.get(0).getName());
        assertEquals("Jane Doe", customers.get(1).getName());
    }

    // You can add more test methods here if needed
}
