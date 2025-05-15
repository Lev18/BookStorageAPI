<<<<<<< HEAD
package com.bookstore.books.controller;import com.bookstore.books.criteria.BookSearchCriteria;import com.bookstore.books.dto.requestDto.AwardDto;import com.bookstore.books.dto.requestDto.BookRequestDto;import com.bookstore.books.dto.responseDto.BookInfoDTO;import com.bookstore.books.dto.responseDto.BookResponseDto;import com.bookstore.books.dto.responseDto.PageResponseDto;import com.bookstore.books.entity.Book;import com.bookstore.books.exception.BookAlreadyExistException;import com.bookstore.books.service.BookService;import jakarta.validation.Valid;import lombok.RequiredArgsConstructor;import lombok.extern.slf4j.Slf4j;import org.springframework.http.HttpStatus;import org.springframework.http.MediaType;import org.springframework.http.ResponseEntity;import org.springframework.web.bind.annotation.*;import org.springframework.web.multipart.MultipartFile;import java.io.IOException;import java.io.InputStream;import java.nio.file.Path;import java.nio.file.Paths;@Slf4j@RestController@RequestMapping(path = "/api/books")@RequiredArgsConstructorpublic class BookController {    private final BookService bookService;    @PostMapping(path = "/file", consumes = {"multipart/form-data"})    public ResponseEntity<?> uploadBookStore(@RequestPart("file") MultipartFile file) {        long startTime = System.nanoTime();        int savedBooks = bookService.uploadAndSaveFile(file);        long endTime = System.nanoTime();        log.info("saved book completed in {} s", (endTime - startTime) / 1_000_000_000.0);        if (savedBooks == 0) {            return ResponseEntity.status(HttpStatus.CONFLICT).build();        }        return ResponseEntity.ok(savedBooks + " new  books were saved\n");    }    @PostMapping()    public ResponseEntity<?> insertBook(@Valid @RequestBody BookRequestDto bookRequestDto) {        BookInfoDTO bookInfoDTO = null;        System.out.println("Received ISBN: " + bookRequestDto.getIsbn());  // Should not be null        try {            bookInfoDTO = bookService.insertNewBook(bookRequestDto);        } catch (BookAlreadyExistException e) {            ResponseEntity.status(HttpStatus.CONFLICT);        }        return ResponseEntity.ok(bookInfoDTO);    }    @DeleteMapping(path = "/{bookIsbn}")    public ResponseEntity<?> deleteBookByISBN(@PathVariable String bookIsbn) {        BookInfoDTO bookInfoDTO = bookService.deleteBookByISBN(bookIsbn);        if (bookInfoDTO == null) {            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();        }        return ResponseEntity.ok().body(bookInfoDTO);    }    @GetMapping(path = "/image/{bookIsbn}")    public ResponseEntity<?> getBookImage(@PathVariable String bookIsbn) throws IOException {        byte[] img = bookService.getBookImg(bookIsbn);        if (img == null || img.length == 0) {            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();        }        return ResponseEntity.ok()                .contentType(MediaType.IMAGE_JPEG)                .body(img);    }    @GetMapping(path = "/{isbn}")    public ResponseEntity<BookInfoDTO> getBookByISBN(@PathVariable String isbn) {        BookInfoDTO bookInfoDTO = bookService.getBookByISBN(isbn);        if (bookInfoDTO == null) {            return ResponseEntity.notFound().build();        }        return ResponseEntity.ok().body(bookInfoDTO);    }    @PutMapping(path = "/rating/{bookIsbn}/{newRate}")    public ResponseEntity<?> updateBookRating(@PathVariable String bookIsbn,                                              @PathVariable Integer newRate) {        Book updatedBook = bookService.updateBookRating(bookIsbn, newRate);        if (updatedBook == null) {            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Book not found\n");        }        return ResponseEntity.ok(updatedBook.getBookId() + " book's rating was updated successfully");    }    @PostMapping(path = "/newAward/{bookIsbn}")    public ResponseEntity<?> addNewAward(@PathVariable String bookIsbn, @Valid @RequestBody AwardDto newAward) {        String bookInfoDTO = bookService.addNewAward(bookIsbn, newAward);        if (bookInfoDTO == null) {            return ResponseEntity.notFound().build();        }        return ResponseEntity.ok(newAward.getAward() + " award added " + bookInfoDTO + " successfully");    }    @GetMapping(path = "/search")    public PageResponseDto<BookResponseDto> findAllBooksByCriteria(BookSearchCriteria bookSearchCriteria) {        return bookService.findAllByCriteria(bookSearchCriteria);    }    // TODO: move into BookService    @GetMapping(path = "/images/{directory}/{img}")    public ResponseEntity<byte[]> getBookImage(@PathVariable String directory,                                               @PathVariable String img) throws IOException {        Path path = Paths.get("/home/levon/Workspace/ImagesBook/resized/" + directory + "/" + img);        try (InputStream in = path.toUri().toURL()                .openStream()) {//            File file = new File("/images/169413.jpg");//            Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);            byte[] imgBytes = in.readAllBytes();            return ResponseEntity.ok()                    .contentType(MediaType.IMAGE_JPEG)                    .body(imgBytes);        }    }    //TODO: get top rated books}
=======
package com.bookstore.books.controller;

import com.bookstore.books.criteria.BookSearchCriteria;
import com.bookstore.books.dto.requestDto.AwardDto;
import com.bookstore.books.dto.requestDto.BookRequestDto;
import com.bookstore.books.dto.responseDto.BookInfoDTO;
import com.bookstore.books.dto.responseDto.BookResponseDto;
import com.bookstore.books.dto.responseDto.PageResponseDto;
import com.bookstore.books.entity.Book;
import com.bookstore.books.exception.BookAlreadyExistException;
import com.bookstore.books.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RestController
@RequestMapping(path = "/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping(path = "/file", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadBookStore(@RequestPart("file") MultipartFile file) {
        long startTime = System.nanoTime();
        int savedBooks = bookService.uploadAndSaveFile(file);
        long endTime = System.nanoTime();
        log.info("saved book completed in {} s", (endTime - startTime) / 1_000_000_000.0);
        if (savedBooks == 0) {
            //TODO:Throw an error
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        return ResponseEntity.ok(savedBooks + " new  books were saved\n");
    }


    @PostMapping()
    public ResponseEntity<?> insertBook(@Valid @RequestBody BookRequestDto bookRequestDto) {
        BookInfoDTO bookInfoDTO = null;
        System.out.println("Received ISBN: " + bookRequestDto.getIsbn());  // Should not be null

        try {
            bookInfoDTO = bookService.insertNewBook(bookRequestDto);

        } catch (BookAlreadyExistException e) {
            ResponseEntity.status(HttpStatus.CONFLICT);
        }

        return ResponseEntity.ok(bookInfoDTO);
    }

    @DeleteMapping(path = "/{bookIsbn}")
    public ResponseEntity<?> deleteBookByISBN(@PathVariable String bookIsbn) {
        BookInfoDTO bookInfoDTO = bookService.deleteBookByISBN(bookIsbn);
        if (bookInfoDTO == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok().body(bookInfoDTO);
    }

    @GetMapping(path = "/image/{bookIsbn}")
    public ResponseEntity<?> getBookImage(@PathVariable String bookIsbn) throws IOException {
        byte[] img = bookService.getBookImg(bookIsbn);
        if (img == null || img.length == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(img);
    }

    @GetMapping(path = "/{isbn}")
    public ResponseEntity<BookInfoDTO> getBookByISBN(@PathVariable String isbn) {
        BookInfoDTO bookInfoDTO = bookService.getBookByISBN(isbn);
        if (bookInfoDTO == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok().body(bookInfoDTO);
    }

    @PutMapping(path = "/rating/{bookIsbn}/{newRate}")
    public ResponseEntity<?> updateBookRating(@PathVariable String bookIsbn,
                                              @PathVariable Integer newRate) {
        Book updatedBook = bookService.updateBookRating(bookIsbn, newRate);
        if (updatedBook == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Book not found\n");
        }
        return ResponseEntity.ok(updatedBook.getBookId() + " book's rating was updated successfully");
    }

    @PostMapping(path = "/newAward/{bookIsbn}")
    public ResponseEntity<?> addNewAward(@PathVariable String bookIsbn, @Valid @RequestBody AwardDto newAward) {
        String bookInfoDTO = bookService.addNewAward(bookIsbn, newAward);
        if (bookInfoDTO == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Book not found\n");
        }
        return ResponseEntity.ok(newAward.getAward() + " award added " + bookInfoDTO + " successfully");
    }


    @GetMapping(path = "/search")
    public PageResponseDto<BookResponseDto> findAllBooksByCriteria(BookSearchCriteria bookSearchCriteria) {
        return bookService.findAllByCriteria(bookSearchCriteria);
    }

    // TODO: move into BookService
    @GetMapping(path = "/images/{directory}/{img}")
    public ResponseEntity<byte[]> getBookImage(@PathVariable String directory,
                                               @PathVariable String img) throws IOException {

        Path path = Paths.get("/home/levon/Workspace/ImagesBook/resized/" + directory + "/" + img);
        try (InputStream in = path.toUri().toURL()
                .openStream()) {
//            File file = new File("/images/169413.jpg");
//            Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            byte[] imgBytes = in.readAllBytes();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imgBytes);
        }
    }
}
>>>>>>> c4198a94c11fec6eb2a21006406099eb0a218835
