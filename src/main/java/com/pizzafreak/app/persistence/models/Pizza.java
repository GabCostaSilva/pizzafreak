package com.pizzafreak.app.persistence.models;

import javax.persistence.*;

@Entity
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private double price;

    public Pizza(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Pizza() {
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }
}
