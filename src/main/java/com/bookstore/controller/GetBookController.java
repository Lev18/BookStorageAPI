package com.bookstore.controller;

import com.bookstore.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class GetBookController {
    private final BookService bookService;
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
