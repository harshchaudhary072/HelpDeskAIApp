package com.substring.helpdesk.service;

import org.springframework.stereotype.Service;

import com.substring.helpdesk.entity.Ticket;
import com.substring.helpdesk.repository.TicketRepository;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;

@Service
@Getter
@Setter
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

  //create ticket

    @Transactional
    public Ticket createTicket(Ticket ticket) {
        ticket.setId(null);
        return ticketRepository.save(ticket);
    }

    public Ticket updateTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public Ticket getTicket(Long ticketId) {
        return ticketRepository.findById(ticketId).orElse(null);
    }

    public Ticket getTicketByEmailId(String username) {
        return ticketRepository.findByEmail(username).orElse(null);
    }
}
