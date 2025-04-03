package com.bookstore.Service;

import com.bookstore.Service.dto.BookCsvDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ApplicationService {
    void saveBook(List<BookCsvDto> bookCsvDtos);
}
