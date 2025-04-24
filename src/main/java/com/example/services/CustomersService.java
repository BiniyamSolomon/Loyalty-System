//package com.example.services;
//
//import com.example.dao.Customer;
//import jakarta.inject.Singleton;
//import jakarta.persistence.*;
//import jakarta.persistence.PersistenceContext;
//import jakarta.transaction.Transactional;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.List;
//
//@Singleton
//@Slf4j
//public class CustomersService {
//
//    @PersistenceContext
//    EntityManager em;
//
//    @Transactional
//    public List<Customer> list() {
//        return em
//            .createQuery("SELECT c FROM Customer c", Customer.class)
//            .getResultList();
//    }
//
//    @Transactional
//    public Customer find(int id) {
//        return em
//            .createQuery("SELECT c FROM Customer c WHERE c.id = :customerId", Customer.class)
//            .setParameter("customerId", id)
//            .getSingleResult();
//    }
//
//    @Transactional
//    public Customer update(int customerId, String name, String surname) {
//        var customer = em.find(Customer.class, customerId);
//        customer.setName(name);
//        customer.setSurname(surname);
//        em.merge(customer);
//        log.debug("updated customer {}", customerId);
//        return customer;
//    }
//
//
//    @Transactional
//    public Customer insert(Customer data){
//
//    }
//
//
//}



package com.example.services;

import com.example.dao.Customer;
import com.example.dao.Transaction;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Singleton
@Slf4j
public class CustomersService {

    @PersistenceContext
    private EntityManager em;

    /** Sign-up a new customer */
    @Transactional
    public Customer signUp(String name, String surname) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setSurname(surname);
        customer.setPoints(0);
        em.persist(customer);
        return customer;
    }

//    /** Retrieve full profile (includes points) */
//    @Transactional
//    public Customer getProfile(int customerId) {
//        return em.find(Customer.class, customerId);
//    }


    @Transactional
    public Customer getProfile(int id){
        return em
                .createQuery("select c from Customer c where c.id = :cid", Customer.class)
                .setParameter("cid", id)
                .getSingleResult();

    }
    /** Retrieve just the point balance */
//    @Transactional
//    public int getPoints(int customerId) {
//        Customer c = em.find(Customer.class, customerId);
//        if(c == null){
//            return 0;
//        }else{
//            return c.getPoints();
//        }

    //retrieve just the point balance
    @Transactional
    public int getPoints(int id){
        Customer customer = em.find(Customer.class, id);
        return customer.getPoints();
    }

    // Retrieve transaction history for this customer
    @Transactional
    public List<Transaction> getTransactionHistory(int customerId) {
        return em.createQuery(
                        "SELECT t FROM Transaction t WHERE t.customer.id = :cid",
                        Transaction.class)
                .setParameter("cid", customerId)
                .getResultList();
    }

    //updates all customer's profile
//    @Transactional
//    public Customer updateProfile(int customerId, String name, String surname) {
//        Customer customer = em.find(Customer.class, customerId);
//        if (customer != null) {
//            customer.setName(name);
//            customer.setSurname(surname);
//            em.merge(customer);
//            log.debug("Updated customer {} profile", customerId);
//        }
//        return customer;
//    }


    @Transactional
    public Customer updateProfile(int id, String name, String surname){
        Customer customer =  em.find(Customer.class, id);
        customer.setName(name);
        customer.setSurname(surname);
        em.merge(customer);
        return customer;
    }
}
