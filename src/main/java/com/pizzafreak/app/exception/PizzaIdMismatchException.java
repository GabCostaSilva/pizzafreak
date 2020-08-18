package com.pizzafreak.app.exception;

public class PizzaIdMismatchException extends Exception {
    public PizzaIdMismatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PizzaIdMismatchException() {
        super();
    }

}
