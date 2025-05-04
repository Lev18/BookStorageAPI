package com.bookstore.users.service.dto;

public record RegisterResponseDto( Long id,
                                   String username,
                                   String email,
                                   String role) {

}
