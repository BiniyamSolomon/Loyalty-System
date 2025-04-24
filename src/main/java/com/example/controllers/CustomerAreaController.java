package com.example.controllers;

import com.example.dao.Customer;
import com.example.dao.Transaction;
import com.example.services.CustomersService;
import io.micronaut.http.annotation.*;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@PermitAll
@Controller("/customers-area")
public class CustomerAreaController {

    @Inject
    private CustomersService customersService;

    /** Sign up a new customer */
    @Post("/signup")
    public Customer signUp(@Body CustomerInput input) {
        return customersService.signUp(input.getName(), input.getSurname());
    }

    /** Get full profile (including points) */
    @Get("/customers/{id}")
    public Customer getProfile(@PathVariable int id) {
        return customersService.getProfile(id);
    }

    /** Get only the point balance */
    @Get("/customers/{id}/points")
    public int getPoints(@PathVariable int id) {
        return customersService.getPoints(id);
    }

    //retrieves all transactions made
    @Get("/customers/{id}/transactions")
    public List<Transaction> getTransactionHistory(@PathVariable int id) {
        return customersService.getTransactionHistory(id);
    }

    // Edit profile/personal data
    @Put("/customers/{id}")
    public Customer updateProfile(
            @PathVariable int id,
            @Body CustomerInput input) {
        return customersService.updateProfile(id, input.getName(), input.getSurname());
    }

    @Getter @Setter @Serdeable
    static class CustomerInput {
        private String name;
        private String surname;
    }
}
