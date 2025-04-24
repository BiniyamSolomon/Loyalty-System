/*

package com.example.controllers;

import com.example.dao.Customer;
import com.example.services.CustomersService;
import io.micronaut.http.annotation.*;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

//@PermitAll
//@Controller


public class MerchantAreaController {

    @Inject
    CustomersService customersService;

    @Get("/merchant-area/customers")
    public List<Customer> list() {
        return customersService.list();
    }

    @Get("/merchant-area/customers/{id}")
    public Customer find(@PathVariable int id) {
        return customersService.find(id);
    }

    @Post("/merchant-area/customers/{id}")
    public Customer find(@PathVariable int id, @Body CustomerInput input) {
        return customersService.update(id, input.getName(), input.getSurname());
    }

    @Getter @Setter @Serdeable
    static class CustomerInput {
        String name;
        String surname;
    }


    @Post("/customers/add")
    public Customer insert(@Body Customer data) {
        customersService.insert(data);
        return "h";
    }
}

 */


package com.example.controllers;

import com.example.dao.Customer;
import com.example.dao.Transaction;
import com.example.dto.SummaryDTO;
import com.example.services.MerchantsService;
import io.micronaut.http.annotation.*;
import io.micronaut.serde.annotation.Serdeable;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@PermitAll
@Controller("/merchant-area")
public class MerchantAreaController {

    @Inject
    private MerchantsService merchantsService;

    // List all customers
    @Get("/customers")
    public List<Customer> listCustomers() {
        return merchantsService.listCustomers();
    }

    /** Edit customer personal data */
    @Put("/customers/{id}")
    public Customer editCustomer(
            @PathVariable int id,
            @Body CustomerInput input) {
        return merchantsService.editCustomer(id, input.getName(), input.getSurname());
    }

    /** Add or remove points for a customer */
    @Post("/transactions")
    public Transaction adjustPoints(@Body PointsAdjustment input) {
        return merchantsService.adjustPoints(
                input.getCustomerId(),
                input.getMerchantId(),
                input.getPoints(),
                input.getDescription()
        );
    }

    /** Retrieve general list of all transactions */
    @Get("/transactions")
    public List<Transaction> listAllTransactions() {
        return merchantsService.listAllTransactions();
    }

    /** Retrieve transactions for a specific customer */
    @Get("/transactions/customer/{id}")
    public List<Transaction> listTransactionsForCustomer(@PathVariable int id) {
        return merchantsService.listTransactionsForCustomer(id);
    }

    /** Retrieve summary data: number of customers & total points */
    @Get("/summary")
    public SummaryDTO getSummary() {
        return merchantsService.getSummary();
    }



    @Getter @Setter @Serdeable
    static class CustomerInput {
        private String name;
        private String surname;
    }

    @Getter @Setter @Serdeable
    static class PointsAdjustment {
        private int customerId;
        private int merchantId;
        private int points;
        private String description;
    }
}
