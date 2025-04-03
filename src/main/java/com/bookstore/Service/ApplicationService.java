package com.bookstore.Service;

import com.bookstore.Service.dto.BookCsvDto;

import java.util.List;

public interface ApplicationService {
    int saveBook(List<BookCsvDto> bookCsvDtos);
}
