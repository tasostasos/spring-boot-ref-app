package com.example.demo.rest;

import com.example.demo.dto.KafkaMessageDTO;
import com.example.demo.entity.Customer;
import com.example.demo.dao.CustomerDAO;
import com.example.demo.kafka.KafkaProducerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.security.Principal;
import java.util.Map;

/**
 * Customer controller ,open for / url,keycloak authenticated for all other endpoints.
 * Uses Thymeleaf templates for response rendering.
 */
@Controller
@Slf4j
public class customerController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @Autowired
    private CustomerDAO customerDAO;

    @GetMapping(path = "/")
    public String index() {
        return "external";
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request) throws Exception {
        request.logout();
        return "redirect:/";
    }

    /**
     * Authenticated via From oidc login page -Principal is {@link DefaultOidcUser}
     *
     * @param principal
     * @param model
     * @return
     */
    @GetMapping(path = "/customers")
    public String customers(Principal principal, Model model) {
        final DefaultOidcUser user = (DefaultOidcUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OidcIdToken token = user.getIdToken();
        log.debug(token.getTokenValue());
        Map<String, Object> customClaims = token.getClaims();

        addCustomers();
        model.addAttribute("customers", customerDAO.findAll());
        model.addAttribute("username", customClaims.get("given_name"));
        return "customers";
    }

    /**
     * Authenticated viaFrom Jwt Bearer authentication token -Principal is {@link Jwt}
     *
     * @param principal
     * @param model
     * @return
     */
    @GetMapping(path = "/customers-jwt")
    public String customersJwt(Principal principal, Model model) {
        Jwt user = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> customClaims = user.getClaims();

        addCustomers();
        model.addAttribute("customers", customerDAO.findAll());
        model.addAttribute("username", customClaims.get("given_name"));
        return "customers";
    }

    // add customers for demonstration
    public boolean addCustomers() {
        if (customerDAO.count() != 0)
            return false;

        Customer customer1 = new Customer();
        customer1.setAddress("1111 foo blvd");
        customer1.setName("Foo Industries");
        customer1.setServiceRendered("Important services");
        customerDAO.save(customer1);

        Customer customer2 = new Customer();
        customer2.setAddress("2222 bar street");
        customer2.setName("Bar LLP");
        customer2.setServiceRendered("Important services");
        customerDAO.save(customer2);

        Customer customer3 = new Customer();
        customer3.setAddress("33 main street");
        customer3.setName("Big LLC");
        customer3.setServiceRendered("Important services");
        customerDAO.save(customer3);

        return true;
    }


    @PostMapping("/kafka-produce")
    public String produceKafkaMessage(@RequestBody KafkaMessageDTO msg) {
        kafkaProducerService.produce(msg);
        return "redirect:/";
    }
}
