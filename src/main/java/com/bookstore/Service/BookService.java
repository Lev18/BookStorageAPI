package com.bookstore.Service;

import com.bookstore.Model.Book;
import com.bookstore.Repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookService {
    @Autowired
    BookRepository bookRepository;
    public Book updateBookRating(String bookId, String newRate) {
        Book book = bookRepository.findBookByBookId(bookId);
        if (book != null) {
            book.setRating(newRate);
            bookRepository.save(book);
        }

        return book;
    }
}
