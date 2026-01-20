package com.substring.helpdesk.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.AdvisorSpec;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import com.substring.helpdesk.HelpDeskBackendApplication;
import com.substring.helpdesk.tools.EmailTool;
import com.substring.helpdesk.tools.TicketDatabaseTool;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import reactor.core.publisher.Flux;

@Service
@Getter
@Setter
public class AIService {

    private final HelpDeskBackendApplication helpDeskBackendApplication;
	
	private final ChatClient chatClient;
	
	private final TicketDatabaseTool ticketDatabaseTool;
	private final EmailTool emailTool;
	
	@Value("classpath:/helpdesk-system.st")
	private Resource systemPromptResource;
	
	public AIService(ChatClient chatClient, TicketDatabaseTool ticketDatabaseTool, EmailTool emailTool, HelpDeskBackendApplication helpDeskBackendApplication) {
		this.chatClient = chatClient;
		this.ticketDatabaseTool = ticketDatabaseTool;
		this.emailTool = emailTool;
		this.helpDeskBackendApplication = helpDeskBackendApplication;
	}
	
	//basic call to llm
	public String getResponseFromAssistant(String query, String conversationId) {
		return this.chatClient.prompt().advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId)).tools(ticketDatabaseTool, emailTool).system(systemPromptResource).user(query).call().content();
	}
	
	public Flux<String> streamResponseFromAssistant(String query, String conversationId) {


        return this.chatClient
                .prompt()
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, conversationId))
                //tool informations
                .tools(ticketDatabaseTool, emailTool)
                .system(systemPromptResource)
                .user(query)
                .stream().content();

    }
	
	
	

}