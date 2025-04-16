package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.service.responceDto.BookInfoDTO;
import com.bookstore.service.responceDto.GenreInfoDTO;
import com.bookstore.utils.imageLoader.ImageLoaderService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;

@Component
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final ImageLoaderService imageLoader;
    private final EntityManager entityManager;
    public Book updateBookRating(String bookId, String newRate) {
        Book book = bookRepository.findBookByBookId(bookId);
        if (book != null) {
            book.setRating(newRate);
            bookRepository.save(book);
        }
        return book;
    }

    public void uploadImg() throws  InterruptedException{
        Semaphore semaphore = new Semaphore(20);
        List<String> books = bookRepository.getAllUrls();
        for (String bookUrl : books) {
            semaphore.acquire();
            CompletableFuture .runAsync(() -> {
                try {
                    imageLoader.downloadImage(bookUrl);

                } finally {
                    semaphore.release();
                }
            });
        }
    }

    public List<GenreInfoDTO> getAllBooksByGenre(String genre) {
        TypedQuery<GenreInfoDTO> query
               = entityManager.createQuery(
          "select new BookGenreInfoDTO(b.title, g.genreTitle, b.isbn) " +
                  "from Book b " +
                  "join BookGenre bg on b.id = bg.book.id " +
                  "join Genre g on g.id = bg.genre.id " +
                  "where genreTitle = :genreTitle " +
                  "order by bg.id " +
                  "limit :showLimit" ,
                GenreInfoDTO.class
        );
        query.setParameter("genreTitle", genre);
        query.setParameter("showLimit", 100);


        return query.getResultList();
    }

    public BookInfoDTO getBookByISBN(String id) {

//        Book bookGenre = bookRepository.findBookWithGenres(id);
//        Book bookAward = bookRepository.findBookWithAwards(id);
        Book book = bookRepository.findBookWithAttributes(id);

        List<String> genres = book.getGenres()
                .stream()
                .map(bg-> bg.getGenre().getGenreTitle())
                .toList();

        List<String> awards = book.getAwards()
                .stream()
                .map(bg-> bg.getAward().getName())
                .toList();

        return new BookInfoDTO(book.getTitle(),
                                book.getIsbn(),
                                awards,
                                genres);
    }

}





