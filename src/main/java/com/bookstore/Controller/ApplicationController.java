package com.bookstore.Controller;

import com.bookstore.Service.ApplicationService;
import com.bookstore.Service.ApplicationServiceImpl;
import com.bookstore.Service.FileReader.CsvReaderService;
import com.bookstore.Service.dto.BookCsvDto;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
public class ApplicationController {
    private final CsvReaderService csvReaderService;
    @Autowired
    @Qualifier("appServiceImpl")
    private  ApplicationService applicationService;
    List<BookCsvDto> books = new ArrayList<>();

    public ApplicationController(CsvReaderService csvReaderService) {
        this.csvReaderService = csvReaderService;
    }

    @GetMapping
    public String front (){
        return "Welcome";
    }

    @PostMapping(path = "/insert-book")
    @ResponseStatus(code = HttpStatus.CREATED)
    public BookCsvDto addBook(@RequestBody BookCsvDto bookDto) {
        books.add(bookDto);
        return bookDto;
    }

    @PostMapping(path = "/upload-file",consumes = {"multipart/form-data"})
    @SneakyThrows
    public ResponseEntity<?> uploadBookStore(@RequestPart("file") MultipartFile file){
        List<BookCsvDto> csvDTos = csvReaderService.uploadBooks(file);
        if (csvDTos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("File already parsed NO new book");
        }

        int savedBook = applicationService.saveBook(csvDTos);
        return ResponseEntity.ok(savedBook + " new book were added");

    }
}
