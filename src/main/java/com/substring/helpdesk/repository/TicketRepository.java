package com.substring.helpdesk.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.substring.helpdesk.entity.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
	//Optional<Ticket> findByTicketId(Long ticketId);
	Optional<Ticket> findByEmail(String email);
}
