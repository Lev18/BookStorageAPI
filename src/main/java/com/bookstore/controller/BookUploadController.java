package com.bookstore.controller;

import com.bookstore.entity.Book;
import com.bookstore.service.BookService;
import com.bookstore.service.BookUploadService;
import com.bookstore.service.responceDto.BookInfoDTO;
import com.bookstore.service.responceDto.GenreInfoDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BookUploadController {
    private final BookUploadService bookUploadService;
    private final BookService bookService;

    @GetMapping
    public String front() {
        return "Welcome";
    }

    @PostMapping(path = "/books/file", consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadBookStore(@RequestPart("file") MultipartFile file) {
        int savedBooks = bookUploadService.uploadAndSaveFile(file);
        if (savedBooks == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("File already parsed NO new book\n");
        }
        return ResponseEntity.ok(savedBooks + " new  books were saved\n");
    }

    @PutMapping(path = "/update/book/rating/{book_id}/{newRate}")
    public ResponseEntity<?> updateBookRating(@PathVariable String book_id, @PathVariable String newRate) {
        Book updatedBook = bookService.updateBookRating(book_id, newRate);
        if (updatedBook == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Book not found\n");
        }
        return ResponseEntity.ok(updatedBook.getBookId() + " book's rating was updated successfully");
    }

    @GetMapping (path = "/download/images")
    public ResponseEntity<?> uploadImg() throws InterruptedException {
        bookService.uploadImg();
        return ResponseEntity.ok(" images was downloaded successfully");
    }

    @GetMapping(path = "/images/{directory}/{img}")
    public ResponseEntity<byte[]> getImage(@PathVariable String directory,
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
    @GetMapping(path = "/book/image/{bookId}")
  public ResponseEntity<?> getBookImage(@PathVariable String bookId) throws IOException {
//        String bookImgUrl = bookRepository.getUrlById();
//        String[] urlComponents = bookImgUrl.split("/");
//        String img = urlComponents[urlComponents.length - 1];
//        String directory = urlComponents[urlComponents.length - 2];
//
//        Path path = Paths.get("/home/levon/Workspace/ImagesBook/resized/" + directory + "/" + img);
//        try (InputStream in = path.toUri().toURL()
//                .openStream()) {
//
//            byte[] imgBytes = in.readAllBytes();
//            return ResponseEntity.ok()
//                    .contentType(MediaType.IMAGE_JPEG)
//                    .body(imgBytes);
//        }
        return ResponseEntity.ok().body("");
    }

    @GetMapping(path = "/genres/{genre}")
    public ResponseEntity<List<GenreInfoDTO>> getAllBooksByGenre(@PathVariable String genre) {
        List<GenreInfoDTO> allBooksByGenre= bookService.getAllBooksByGenre(genre);
        return ResponseEntity.ok().body(allBooksByGenre);
    }

    @GetMapping(path = "/show/book/{isbn}")
    public ResponseEntity<BookInfoDTO> getBookByISBN(@PathVariable String isbn) {
        BookInfoDTO bookInfoDTO = bookService.getBookByISBN(isbn);
        return ResponseEntity.ok().body(bookInfoDTO);
    }
}

