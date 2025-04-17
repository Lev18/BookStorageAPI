package com.bookstore.controller;

import com.bookstore.dto.responceDto.BookInfoDTO;
import com.bookstore.dto.responceDto.GenreInfoDTO;
import com.bookstore.service.BookService;
import com.bookstore.service.BookUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class GetBookController {
    private final BookService bookService;

    @GetMapping(path = "/book/image/{bookIsbn}")
    public ResponseEntity<?> getBookImage(@PathVariable String bookIsbn) throws IOException {
        byte[] img = bookService.getBookImg(bookIsbn);
        if (img == null || img.length == 0) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(img);
    }

    @GetMapping(path = "/genres/{genre}")
    public ResponseEntity<List<GenreInfoDTO>> getAllBooksByGenre(@PathVariable String genre) {
        List<GenreInfoDTO> allBooksByGenre= bookService.getAllBooksByGenre(genre);
        return ResponseEntity.ok().body(allBooksByGenre);
    }

    @GetMapping(path = "/show/book/{isbn}")
    public ResponseEntity<BookInfoDTO> getBookByISBN(@PathVariable String isbn) {
        BookInfoDTO bookInfoDTO = bookService.getBookByISBN(isbn);
        if (bookInfoDTO == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok().body(bookInfoDTO);
    }

    // TODO: move into BookService
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
}
