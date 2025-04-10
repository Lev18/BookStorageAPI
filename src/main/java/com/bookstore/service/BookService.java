package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.utils.imageLoader.ImageLoader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    public Book updateBookRating(String bookId, String newRate) {
        Book book = bookRepository.findBookByBookId(bookId);
        if (book != null) {
            book.setRating(newRate);
            bookRepository.save(book);
        }
        return book;
    }

    public void uploadImg() {
        List<Book> books = bookRepository.findAll();
        for (Book book : books) {
            ImageLoader.uploadImage(book.getCoverImg());
        }
    }

}





