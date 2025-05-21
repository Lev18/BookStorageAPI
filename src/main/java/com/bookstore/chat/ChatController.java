package com.bookstore.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/llm")
@CrossOrigin(origins = "http://localhost:3000")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @PostMapping("/chat")
    public ResponseEntity<ChatResponseDto> recommendBooks(@RequestBody PromptRequestDTO prompt) {
        try {
            String message = prompt.getPrompt();

            if (message.isEmpty()) {
                System.out.println("Please enter valid prompt");
            }
            return ResponseEntity.ok(
                    chatService.getChatResponse(message));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
