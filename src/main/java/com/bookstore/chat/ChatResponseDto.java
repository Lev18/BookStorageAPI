package com.bookstore.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@RequiredArgsConstructor
public class ChatResponseDto {
    public ChatResponseDto(String answer) {
        this.answer = answer;
    }
    private String answer;
}
