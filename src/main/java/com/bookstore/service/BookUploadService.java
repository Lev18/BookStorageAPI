package com.bookstore.service;

import com.bookstore.entity.*;
import com.bookstore.repository.*;
import com.bookstore.service.dto.BookCsvDto;
import com.bookstore.service.mapper.BookDtoToBookDBMapper;
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
public class ApplicationServiceImpl implements ApplicationService{

    private final BookDtoToBookDBMapper bookDtoToBookDBMapper;
    private final BookToGenreMapper bookToGenreMapper;
    private final BookRepository bookRepository;
    private final AwardsRepository awardsRepository;
    private final CharactersRepository charactersRepository;
    private final BookGenreRepository bookGenreRepository;
    private final GenreRepository genreRepository;
    private final RatingByStarsRepository ratingByStarsRepository;

    @Transactional
    public int saveBook(List<BookCsvDto> bookCsvDtos) {
        List<Book> allBooks = new ArrayList<>();
        List<BookGenre> allBookGenres = new ArrayList<>();
        Set<Awards> allAwards = new HashSet<>();
        Set<RatingByStars> ratingByStars = new HashSet<>();
        Set<Characters> allCharacters = new HashSet<>();
        Set<Genre> allGenres = new HashSet<>();

        for (BookCsvDto bookCsvDto : bookCsvDtos) {
//            ImageLoader.uploadImage(bookCsvDto.getCoverImg());
            Book book = bookDtoToBookDBMapper.bookToAwardMapper(bookCsvDto);
            allBooks.add(book);
            allAwards.addAll(book.getAwards());
            ratingByStars.addAll(book.getStars());
            allCharacters.addAll(book.getCharacters());
            // TODO: how collect genres into set
            // Optimize genre creation process to not query database for each book genres
            List<Genre> bookGenres = bookToGenreMapper.findOrCreateGenre(bookCsvDto, genreRepository);
            allGenres.addAll(bookGenres);
            for (Genre genre : bookGenres) {
                BookGenre bookGenre = new BookGenre();
                bookGenre.setBook(book);
                bookGenre.setGenre(genre);
                allBookGenres.add(bookGenre);
            }
        }

        bookGenreRepository.saveAll(allBookGenres);
        genreRepository.saveAll(allGenres);
        bookRepository.saveAll(allBooks);
        awardsRepository.saveAll(allAwards);
        ratingByStarsRepository.saveAll(ratingByStars);
        charactersRepository.saveAll(allCharacters);

        return bookCsvDtos.size();
    }

}
