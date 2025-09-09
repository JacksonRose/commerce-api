package com.example.commerceapi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CartFlowTest {
    
    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate rest;

    @Test
    void cart_add_view_checkout_happy_path(){
        String base = "http://localhost:" + port;

        ResponseEntity<String> add1 = rest.postForEntity(base +"/cart/add?session=demo&id=1", null, String.class); 
        ResponseEntity<String> add2 = rest.postForEntity(base +"/cart/add?session=demo&id=2", null, String.class);
        assertThat(add1.getStatusCodeValue()).isEqualTo(200);
        assertThat(add2.getStatusCodeValue()).isEqualTo(200);

        String view = rest.getForObject(base + "/cart/view?session=demo", String.class);
        assertThat(view).contains("\"size\":2");

        ResponseEntity<String>  checkout = rest.postForEntity(base + "/cart/checkout?session=demo", null, String.class);
        assertThat(checkout.getStatusCodeValue()).isEqualTo(200);
        assertThat(checkout.getBody()).contains("\"status\":\"OK\"");
    }
}
