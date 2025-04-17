package com.bookstore.service;

import com.bookstore.entity.Book;
import com.bookstore.entity.BookAuthor;
import com.bookstore.entity.BookAward;
import com.bookstore.entity.Genre;
import com.bookstore.enums.Language;
import com.bookstore.exception.ImageNotFound;
import com.bookstore.repository.BookRepository;
import com.bookstore.dto.responceDto.BookInfoDTO;
import com.bookstore.dto.responceDto.GenreInfoDTO;
import com.bookstore.repository.GenreRepository;
import com.bookstore.utils.imageLoader.ImageLoaderService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final ImageLoaderService imageLoader;
    private final EntityManager entityManager;
    public Book updateBookRating(String bookIsbn, String newRate) {
        Book book = bookRepository.findBookByIsbn(bookIsbn);
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

    @SneakyThrows
    public byte[] getBookImg(String bookIsbn) {
        String bookImgUrl = bookRepository.getUrlById(bookIsbn);
        String[] urlComponents = bookImgUrl.split("/");
        String img = urlComponents[urlComponents.length - 1];
        String directory = urlComponents[urlComponents.length - 2];

        Path path = Paths.get("/home/levon/Workspace/ImagesBook/resized/" + directory + "/" + img);
        try (InputStream in = path.toUri().toURL()
                .openStream()) {

            return in.readAllBytes();
        } catch (FileNotFoundException e) {
            throw new ImageNotFound("Image not found, no such file or directory");
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
        if (book == null) {
            return null;
        }

        List<String> genres = book.getGenres()
                .stream()
                .map(bg-> bg.getGenre().getGenreTitle())
                .toList();

        List<String> awards = book.getAwards()
                .stream()
                .map(BookAward::getBookAward)
                .toList();

        List<String> characters = book.getCharacters()
                .stream()
                .map(bookCharacter -> bookCharacter.getCharacter().getCharacterName())
                .toList();

       List<String> author = book.getAuthor()
                .stream()
                .map(bookAuthor -> bookAuthor.getAuthor().getAuthorName())
                .toList();

        return new BookInfoDTO(book.getTitle(),author,
                                book.getIsbn(),
                                book.getDescription(),
                                book.getPages(),
                                book.getPublishDate(),
                                book.getLanguage().toString(),
                                awards,
                                characters,
                                genres);
    }

    @Transactional
    public BookInfoDTO deleteBookByISBN(String isbn) {
        BookInfoDTO delCandidate = getBookByISBN(isbn);
        bookRepository.deleteBookSettingsByBookIsbn(isbn);
        bookRepository.deleteBookPublisherByBookIsbn(isbn);
        bookRepository.deleteRatingByStarsByBookIsbn(isbn);
        bookRepository.deleteBookGenreByBookIsbn(isbn);
        bookRepository.deleteBookAwardByBookIsbn(isbn);
        bookRepository.deleteBookAuthorByBookIsbn(isbn);
        bookRepository.deleteBookFormatByBookIsbn(isbn);
        bookRepository.deleteBookCharacterByBookIsbn(isbn);
        bookRepository.deleteBookByISBN(isbn);
        return  delCandidate;
    }

    public Genre deleteGenre(String genreName) {
        Genre genre = genreRepository.findByGenreTitle(genreName);
        genreRepository.deleteBookGenresByGenreId(genre.getId());
        genreRepository.delete(genre);
        return genre;
    }
}





