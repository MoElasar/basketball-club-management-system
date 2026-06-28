package com.learning.main.repository.services;

import com.learning.main.enm.TicketStatus;
import com.learning.main.model.*;
import com.learning.main.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;

@Service
public class TicketService {

	private final TicketRepository ticketRepository;
	private final GameRepository gameRepository;
	private final TicketPurchaseRepository ticketPurchaseRepository;

	@Autowired
	public TicketService(TicketRepository ticketRepository, GameRepository gameRepository,
			TicketPurchaseRepository ticketPurchaseRepository) {
		this.ticketRepository = ticketRepository;
		this.gameRepository = gameRepository;
		this.ticketPurchaseRepository = ticketPurchaseRepository;
	}

	public Ticket saveTicket(Ticket ticket) {
		return ticketRepository.save(ticket);
	}

	public Optional<Ticket> getTicketById(Long id) {
		return ticketRepository.findById(id);
	}

	public List<Ticket> getAllTickets() {
		return ticketRepository.findAll();
	}

	public void deleteTicket(Long id) {
		ticketRepository.deleteById(id);
	}

	public List<Ticket> findTicketsByGame(Game game) {
		return ticketRepository.findByGame(game);
	}

	public List<Ticket> findTicketsByStatus(TicketStatus status) {
		return ticketRepository.findByStatus(status);
	}

	public List<Ticket> findTicketsByGameAndStatus(Game game, TicketStatus status) {
		return ticketRepository.findByGameAndStatus(game, status);
	}

	public List<Game> getUpcomingGames() {
		LocalDateTime now = LocalDateTime.now();
		return gameRepository.findByGameDateTimeBetween(now, now.plusMonths(6));
	}

	public Optional<TicketPurchase> getPurchaseById(Long purchaseId) {
		return ticketPurchaseRepository.findById(purchaseId);
	}

	public List<Ticket> getAvailableTicketsForGame(Long gameId) {
		return ticketRepository.findByGame_GameIdAndStatus(gameId, TicketStatus.AVAILABLE);
	}

	public TicketPurchase savePurchase(TicketPurchase purchase) {
		return ticketPurchaseRepository.save(purchase);
	}
}
