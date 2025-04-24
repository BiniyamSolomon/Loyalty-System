package com.example.services;

import com.example.dao.Customer;
import com.example.dao.Merchant;
import com.example.dao.Transaction;
import com.example.dto.SummaryDTO;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Singleton
@Slf4j
public class MerchantsService {

    @PersistenceContext
    private EntityManager em;

    /** List all customers */
    @Transactional
    public List<Customer> listCustomers() {
        return em.createQuery("SELECT c FROM Customer c", Customer.class)
                .getResultList();
    }

    /** Edit customer data */
    @Transactional
    public Customer editCustomer(int customerId, String name, String surname) {
        Customer c = em.find(Customer.class, customerId);
        if (c != null) {
            c.setName(name);
            c.setSurname(surname);
            em.merge(c);
        }
        return c;
    }

    /** Add or remove points */
    @Transactional
    public Transaction adjustPoints(int customerId, int merchantId, int points, String description) {
        Customer c = em.find(Customer.class, customerId);
        Merchant m = em.find(Merchant.class, merchantId);
        if (c == null || m == null) {
            throw new IllegalArgumentException("Invalid customer or merchant ID");
        }
        // Update balance
        c.setPoints(c.getPoints() + points);
        em.merge(c);

        // Log transaction
        Transaction t = new Transaction();
        t.setCustomer(c);
        t.setMerchant(m);
        t.setPoints(points);
        t.setDescription(description);
        em.persist(t);
        return t;
    }

    /** List all transactions */
    @Transactional
    public List<Transaction> listAllTransactions() {
        return em.createQuery("SELECT t FROM Transaction t", Transaction.class)
                .getResultList();
    }

    /** List transactions for a single customer */
    @Transactional
    public List<Transaction> listTransactionsForCustomer(int customerId) {
        return em.createQuery(
                        "SELECT t FROM Transaction t WHERE t.customer.id = :cid",
                        Transaction.class)
                .setParameter("cid", customerId)
                .getResultList();
    }

    /** Summary: number of customers & total points circulating */
    @Transactional
    public SummaryDTO getSummary() {
        Object[] result = (Object[]) em.createQuery(
                        "SELECT COUNT(c), COALESCE(SUM(c.points),0) FROM Customer c")
                .getSingleResult();
        long count = (Long) result[0];
        long totalPoints = (Long) result[1];
        return new SummaryDTO(count, totalPoints);
    }
}
