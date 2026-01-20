package com.substring.helpdesk.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.substring.helpdesk.entity.Ticket;
import com.substring.helpdesk.service.TicketService;

@Component
public class TicketDatabaseTool {
	
	private final TicketService ticketService;
	
	public TicketDatabaseTool(TicketService ticketService) {
		this.ticketService = ticketService;
	}
	
	//create ticket tool
	@Tool(description="this tool helps to create new ticket in database")
	public Ticket createTicketTool(@ToolParam(description = "Ticket details") Ticket  ticket) {
		try {
            System.out.println("going to create ticket");
            System.out.println(ticket);
            return ticketService.createTicket(ticket);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	
	// get ticket using username
    @Tool(description = "This tool helps to get ticket by username.")
    public Ticket getTicketByUserName(@ToolParam(description = " email id whose ticket is required ") String emailid) {
        return ticketService.getTicketByEmailId(emailid);
    }
	//update ticket tool
	@Tool(description="this tool helps to update existing ticket in database")
	public Ticket updateTicket(@ToolParam(description = "new ticket details with old ticket id") Ticket ticket) {
		return ticketService.updateTicket(ticket);
	}
	
	//get current system time
	@Tool(description="this tool helps to get current system time")
	public String getCurrentTime() {
		return String.valueOf(System.currentTimeMillis()); 
	}
	
	

}
