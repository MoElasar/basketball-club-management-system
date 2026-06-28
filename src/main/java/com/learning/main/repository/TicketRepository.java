package com.learning.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.learning.main.enm.TicketStatus;
import com.learning.main.model.Game;
import com.learning.main.model.Ticket;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	List<Ticket> findByGame(Game game);

	List<Ticket> findByStatus(TicketStatus status);

	List<Ticket> findByGameAndStatus(Game game, TicketStatus status);
	List<Ticket> findByGame_GameIdAndStatus(Long gameId, TicketStatus status);
 

}
