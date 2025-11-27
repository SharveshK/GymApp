package com.gymapp.service;

import com.gymapp.dto.ChatRequest;
import com.gymapp.dto.ChatResponse;
import com.gymapp.model.ChatHistory;
import com.gymapp.model.Users;
import com.gymapp.model.enums.Sender;
import com.gymapp.repository.ChatHistoryRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate; // <-- Import

import java.util.HashMap; // <-- Import
import java.util.List;
import java.util.Map; // <-- Import
import java.util.stream.Collectors;

@Service
public class ChatHistoryService {

    private final ChatHistoryRepository chatHistoryRepository;
    // We use RestTemplate to talk to Python
    private final RestTemplate restTemplate = new RestTemplate();
    // The URL of your Python script
    private final String AI_SERVICE_URL = "http://localhost:5000/chat";

    public ChatHistoryService(ChatHistoryRepository chatHistoryRepository) {
        this.chatHistoryRepository = chatHistoryRepository;
    }

    @Transactional(readOnly = true)
    public List<ChatResponse> getAllMessagesForCurrentUser() {
        Users currentUser = getAuthenticatedUser();
        return chatHistoryRepository.findByUser_UserIdOrderByCreatedAtAsc(currentUser.getUserId())
                .stream()
                .map(this::mapEntityToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatResponse postMessage(ChatRequest request) {
        Users currentUser = getAuthenticatedUser();

        // 1. Save USER message
        ChatHistory userMessage = ChatHistory.builder()
                .user(currentUser)
                .sender(Sender.USER)
                .messageText(request.getMessageText())
                .isSummarized(false)
                .build();
        chatHistoryRepository.save(userMessage);

        // 2. Call Python AI Service
        String aiReplyText = "Thinking...";
        try {
            // Prepare JSON for Python
            Map<String, Object> pythonRequest = new HashMap<>();
            pythonRequest.put("userId", currentUser.getUserId());
            pythonRequest.put("message", request.getMessageText());

            // Send POST to Python
            Map<String, String> pythonResponse = restTemplate.postForObject(
                    AI_SERVICE_URL,
                    pythonRequest,
                    Map.class
            );

            if (pythonResponse != null && pythonResponse.containsKey("response")) {
                aiReplyText = pythonResponse.get("response");
            } else {
                aiReplyText = "Error: AI did not respond.";
            }

        } catch (Exception e) {
            System.err.println("AI Service Error: " + e.getMessage());
            aiReplyText = "The Oracle is currently offline. (Check Python script)";
        }

        // 3. Save AI message
        ChatHistory aiMessage = ChatHistory.builder()
                .user(currentUser)
                .sender(Sender.AI)
                .messageText(aiReplyText)
                .isSummarized(false)
                .build();
        ChatHistory savedAiMessage = chatHistoryRepository.save(aiMessage);

        return mapEntityToResponse(savedAiMessage);
    }

    private Users getAuthenticatedUser() {
        return (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private ChatResponse mapEntityToResponse(ChatHistory message) {
        return ChatResponse.builder()
                .messageId(message.getMessageId())
                .userId(message.getUser().getUserId())
                .sender(message.getSender())
                .messageText(message.getMessageText())
                .createdAt(message.getCreatedAt())
                .build();
    }
}