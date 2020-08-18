package com.pizzafreak.app;

import com.pizzafreak.app.persistence.models.Pizza;
import static io.restassured.RestAssured.*;

import io.restassured.response.Response;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/* TODO throwing java.lang.AbstractMethodError */
public class BootstrapLiveTest {
    private static final String API_ROOT = "http://localhost:8080/api/pizzas";

    private Pizza createNewPizza() {
        String name = RandomString.make(10);
        double price = (new Random().nextDouble() + 1) * 10;
        return new Pizza(name, price);
    }

    private String createPizzaAsUri(Pizza pizza) {
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pizza)
                .post(API_ROOT);
        return API_ROOT + "/" + response.jsonPath().get("id");
    }

    @Test
    public void whenGetAllBooks_thenOK() {
        Response response = get(API_ROOT);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenGetBooksByTitle_thenOK() {
        Pizza pizza = this.createNewPizza();
        this.createPizzaAsUri(pizza);
        Response response = get(API_ROOT + "/name/" + pizza.getName());

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertTrue(response.as(List.class).size() > 0);
    }

    @Test
    public void whenGetCreatedBookById_thenOK() {
        Pizza pizza = this.createNewPizza();
        String location = this.createPizzaAsUri(pizza);
        Response response = get(location);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(pizza.getName(), response.jsonPath().get("name"));
    }
}
