package com.learning.main.repository.services;
import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
@Service
public class TicketPurchaseService {
    @Autowired
    private TicketPurchaseRepository ticketPurchaseRepository;

    public TicketPurchase saveTicketPurchase(TicketPurchase ticketPurchase) {
        return ticketPurchaseRepository.save(ticketPurchase);
    }

    public Optional<TicketPurchase> getTicketPurchaseById(Long id) {
        return ticketPurchaseRepository.findById(id);
    }

    public List<TicketPurchase> getAllTicketPurchases() {
        return ticketPurchaseRepository.findAll();
    }

    public void deleteTicketPurchase(Long id) {
        ticketPurchaseRepository.deleteById(id);
    }

    public List<TicketPurchase> findTicketPurchasesByCustomer(Customer customer) {
        return ticketPurchaseRepository.findByCustomer(customer);
    }

//    public List<TicketPurchase> findTicketPurchasesByPurchaseDateBetween(LocalDate start, LocalDate end) {
//        return ticketPurchaseRepository.findByPurchaseDateBetween(start, end);
//    }
}