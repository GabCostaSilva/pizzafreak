package com.pizzafreak.app;

import com.pizzafreak.app.persistence.models.Pizza;

import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.assertj.core.internal.bytebuddy.utility.RandomString;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class BootstrapLiveTest{
    @LocalServerPort
    private int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        baseURI = "http://localhost";
        basePath = "/api/pizzas";
    }

    private Pizza createNewPizza() {
        String name = RandomString.make(10);
        double price = (new Random().nextDouble() + 1) * 10;
        return new Pizza(name, price);
    }

    private String createPizzaAsUri(Pizza pizza) {
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pizza)
                .post("/");
        return  response.jsonPath().get("id").toString();
    }

    @Test
    public void whenGetAllBooks_thenOK() {
        Response response = get("/");

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenGetBooksByTitle_thenOK() {
        Pizza pizza = this.createNewPizza();
        this.createPizzaAsUri(pizza);
        Response response = get("/name/" + pizza.getName());

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

    @Test
    public void whenCreateNewBook_thenCreated() {
        Pizza pizza = this.createNewPizza();
        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pizza)
                .post("/");
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    public void whenInvalidPizza_thenError() {
        Pizza pizza = this.createNewPizza();
        pizza.setName(null);

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pizza)
                .post("/");
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
    }

    @Test
    public void whenUpdateCreatedPizza_thenUpdated() {
        Pizza pizza = this.createNewPizza();
        String pizzaId = this.createPizzaAsUri(pizza);
        pizza.setId(Long.parseLong(pizzaId));
        String newName = "Marguerita";
        pizza.setName(newName);

        Response response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(pizza)
                .put(pizzaId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = get(pizzaId);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        assertEquals(newName, response.jsonPath().get("name"));
    }

    @Test
    public void whenDeleteCreatedPizza_thenOK() {
        Pizza pizza = this.createNewPizza();
        String location = this.createPizzaAsUri(pizza);
        Response response = delete(location);

        assertEquals(HttpStatus.OK.value(), response.getStatusCode());

        response = get(location);
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
    }
}
