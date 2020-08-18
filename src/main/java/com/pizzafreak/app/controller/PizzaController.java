package com.pizzafreak.app.controller;

import com.pizzafreak.app.exception.PizzaIdMismatchException;
import com.pizzafreak.app.exception.PizzaNotFoundException;
import com.pizzafreak.app.persistence.models.Pizza;
import com.pizzafreak.app.persistence.repositories.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
public class PizzaController {

    private final PizzaRepository pizzaRepository;

    @Autowired
    public PizzaController(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    @GetMapping
    public Iterable findAll() {
        return pizzaRepository.findAll();
    }


    @GetMapping("/name/{pizzaName}")
    public List findByName(@PathVariable String pizzaName) {
        return pizzaRepository.findByName(pizzaName);
    }

    @GetMapping("/{id}")
    public Pizza findOne(@PathVariable Long id) throws PizzaNotFoundException {
        return pizzaRepository.findById(id).orElseThrow(PizzaNotFoundException::new);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pizza create(@RequestBody Pizza pizza) {
        return pizzaRepository.save(pizza);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws PizzaNotFoundException {
        pizzaRepository.findById(id).orElseThrow(PizzaNotFoundException::new);
        pizzaRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Pizza updatePizza(@RequestBody Pizza pizza, @PathVariable Long id) throws PizzaIdMismatchException, PizzaNotFoundException {
        if (pizza.getId() != id) {
            throw new PizzaIdMismatchException();
        }
        pizzaRepository.findById(id).orElseThrow(PizzaNotFoundException::new);
        return pizzaRepository.save(pizza);
    }
}
