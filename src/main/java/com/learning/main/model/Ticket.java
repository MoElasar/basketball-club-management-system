package com.learning.main.model;
 
import com.learning.main.enm.TicketStatus;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// This class represents a core business entity, holding data and relationships relevant to tickets.
// Data Mapper Pattern (via JPA annotations)
// JPA annotations are used to map this object to the 'tickets' table in the database.

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;
    
    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;
    
    private String seatNumber;
    private String section;
    private double price;
    
    @Enumerated(EnumType.STRING)
    private TicketStatus status;
    
    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private TicketPurchase purchase;

    // Getters and Setters
    public Long getTicketId() {
        return ticketId;
    }

    public void setTicketId(Long ticketId) {
        this.ticketId = ticketId;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public TicketPurchase getPurchase() {
        return purchase;
    }

    public void setPurchase(TicketPurchase purchase) {
        this.purchase = purchase;
    }
}