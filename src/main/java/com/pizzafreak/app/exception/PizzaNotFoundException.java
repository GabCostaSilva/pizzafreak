package com.pizzafreak.app.exception;

public class PizzaNotFoundException extends Exception {
    public PizzaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PizzaNotFoundException() {
        super();
    }
}
