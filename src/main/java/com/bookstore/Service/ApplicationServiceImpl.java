package com.bookstore.Service;

import com.bookstore.Model.*;
import com.bookstore.Repository.*;
import com.bookstore.Service.dto.BookCsvDto;
import com.bookstore.Service.mapper.BookDtoToBookDBMapper;
import com.bookstore.Service.mapper.BookToGenreMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component("appServiceImpl")
public class ApplicationServiceImpl implements ApplicationService{
    @Autowired
    BookDtoToBookDBMapper bookDtoToBookDBMapper;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    private AwardsRepository awardsRepository;
    @Autowired
    private  CharactersRepository charactersRepository;
    @Autowired
    BookGenreRepository bookGenreRepository;
    @Autowired
    GenreRepository genreRepository;
    @Autowired
    BookToGenreMapper bookToGenreMapper;

    @Transactional
    public int saveBook(List<BookCsvDto> bookCsvDtos) {
        List<Book> allBooks = new ArrayList<>();
        List<BookGenre> allBookGenres = new ArrayList<>();
        Set<Awards> allAwards = new HashSet<>();
        Set<Characters> allCharacters = new HashSet<>();
        Set<Genre> allGenres = new HashSet<>();
        
        for (BookCsvDto bookCsvDto : bookCsvDtos) {
//            ImageLoader.uploadImage(bookCsvDto.getCoverImg());
            Book book = bookDtoToBookDBMapper.bookToAwardMapper(bookCsvDto);
            allBooks.add(book);
            allAwards.addAll(book.getAwards());
            allCharacters.addAll(book.getCharacters());

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
        charactersRepository.saveAll(allCharacters);

        return bookCsvDtos.size();
    }

}
