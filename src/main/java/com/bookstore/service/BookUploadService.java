package com.bookstore.service;

import com.bookstore.entity.*;
import com.bookstore.repository.*;
import com.bookstore.service.dto.BookCsvDto;
import com.bookstore.service.mapper.BookDtoToBookDBMapper;
import com.bookstore.service.mapper.BookToAuthorMapper;
import com.bookstore.service.mapper.BookToGenreMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
// handle preloading of the same elements of
@Component("appServiceImpl")
@AllArgsConstructor
public class BookUploadService {

    private final BookDtoToBookDBMapper bookDtoToBookDBMapper;
    private final BookToGenreMapper bookToGenreMapper;
    private final BookToAuthorMapper bookToAuthorMapper;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final AwardsRepository awardsRepository;
    private final CharactersRepository charactersRepository;
    private final BookGenreRepository bookGenreRepository;
    private final GenreRepository genreRepository;
    private final RatingByStarsRepository ratingByStarsRepository;

    @Transactional
    public int saveBook(List<BookCsvDto> bookCsvDtos) {

        List<Book> allBooks = new ArrayList<>();
        List<BookGenre> allBookGenres = new ArrayList<>();

        List<Author> allNewAuthors = new ArrayList<>();
        List<Genre> allNewGenre = new ArrayList<>();
        List<BookAuthor> allNewBookAuthor = new ArrayList<>();

        Set<Awards> allAwards = new HashSet<>();
        Set<RatingByStars> ratingByStars = new HashSet<>();
        Set<Characters> allCharacters = new HashSet<>();

        Set<Author> allAuthorsInDb = new HashSet<>(authorRepository.findAll());
        Set<Genre> allGenresInDb = new HashSet<>(genreRepository.findAll());

        for (BookCsvDto bookCsvDto : bookCsvDtos) {
            Book book = bookDtoToBookDBMapper.bookToAwardMapper(bookCsvDto);
            allBooks.add(book);
            allAwards.addAll(book.getAwards());
            ratingByStars.addAll(book.getStars());
            allCharacters.addAll(book.getCharacters());

            List<Author> bookAuthors = bookToAuthorMapper.bookToAuthorMapper(bookCsvDto, allAuthorsInDb);
            allNewAuthors.addAll(bookAuthors);

            for (Author author : bookAuthors) {
                BookAuthor bookAuthor = new BookAuthor();
                bookAuthor.setAuthor(author);
                bookAuthor.setBook(book);
                allNewBookAuthor.add(bookAuthor);
            }

            List<Genre> bookGenres = bookToGenreMapper.findOrCreateGenre(bookCsvDto, allGenresInDb);
            allNewGenre.addAll(bookGenres);

            for (Genre genre : bookGenres) {
                BookGenre bookGenre = new BookGenre();
                bookGenre.setBook(book);
                bookGenre.setGenre(genre);
                allBookGenres.add(bookGenre);
            }
        }

        bookGenreRepository.saveAll(allBookGenres);
        genreRepository.saveAll(new ArrayList<>(allNewGenre));
        authorRepository.saveAll(new ArrayList<>(allNewAuthors));
        bookRepository.saveAll(allBooks);
        awardsRepository.saveAll(allAwards);
        ratingByStarsRepository.saveAll(ratingByStars);
        charactersRepository.saveAll(allCharacters);

        allNewAuthors.clear();
        allNewGenre.clear();
        allBookGenres.clear();
        allNewAuthors.clear();

        return bookCsvDtos.size();
    }

}
