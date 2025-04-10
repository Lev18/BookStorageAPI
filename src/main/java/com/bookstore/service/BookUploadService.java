package com.bookstore.service;

import com.bookstore.entity.*;
import com.bookstore.repository.*;
import com.bookstore.service.dto.BookCsvDto;
import com.bookstore.service.exception.UnableParseFile;
import com.bookstore.service.file_reader.CsvReaderService;
import com.bookstore.service.mapper.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

// handle preloading of the same elements of
@Component("appServiceImpl")
@AllArgsConstructor
public class BookUploadService {
    private final CsvReaderService csvReaderService;
    private final BookDtoToBookDBMapper bookDtoToBookDBMapper;
    private final BookToGenreMapper bookToGenreMapper;
    private final BookToAuthorMapper bookToAuthorMapper;
    private final BookDtoToFormatMapper bookDtoToFormatMapper;
    private final BookDtoToAwardMapper bookDtoToAwardMapper;
    private final BookToPublisherMapper bookDtoToPublisherMapper;

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;
    private final BookAuthorRepository bookAuthorRepository;

    private final AwardsRepository awardsRepository;
    private final BookAwardRepository bookAwardRepository;

    private final CharactersRepository charactersRepository;
    private final BookGenreRepository bookGenreRepository;
    private final GenreRepository genreRepository;
    private final FormatRepository formatRepository;
    private final RatingByStarsRepository ratingByStarsRepository;
    private final BookFormatRepository bookFormatRepository;
    private final PublisherRepository publisherRepository;


    @Transactional
        public int saveBook(List<BookCsvDto> bookCsvDtos) {

            List<Book> allBooks = new ArrayList<>();
            List<BookGenre> allBookGenres = new ArrayList<>();
            List<BookAuthor> allNewBookAuthor = new ArrayList<>();
            List<BookFormat> allNewBookFormat = new ArrayList<>();
            List<BookAward> allNewBookAward = new ArrayList<>();
            List<Publisher> newPublishers  = new ArrayList<>();
            Set<RatingByStars> ratingByStars = new HashSet<>();
            Set<Characters> allCharacters = new HashSet<>();

            Set<Author> allAuthorsInDb = new HashSet<>(authorRepository.findAll());
            Set<Genre> allGenresInDb   = new HashSet<>(genreRepository.findAll());
            Set<Format> allFormatsInDb = new HashSet<>(formatRepository.findAll());
            Set<Award> allAwardsInDb   = new HashSet<>(awardsRepository.findAll());
            Set<String> uniqueIsbn = new HashSet<>(bookRepository.getAllISBNs());
            Set<Publisher> publishers = new HashSet<>(publisherRepository.findAll());

            for (BookCsvDto bookCsvDto : bookCsvDtos) {
                if (uniqueIsbn.contains(bookCsvDto.getIsbn())) {
                    continue;
                }

                Book book = bookDtoToBookDBMapper.bookDtoToBookMapper(bookCsvDto);
                book.setPublisher(bookDtoToPublisherMapper.bookToPublisherMapper(bookCsvDto,
                                                            publishers, newPublishers));
                allBooks.add(book);
                ratingByStars.addAll(book.getStars());
                allCharacters.addAll(book.getCharacters());
                uniqueIsbn.add(bookCsvDto.getIsbn());

                //TODO:refactor to separate function
                List<Author> allNewAuthors = bookToAuthorMapper
                                        .bookToAuthorMapper(bookCsvDto, allAuthorsInDb);
                for (Author author : allNewAuthors) {
                    BookAuthor bookAuthor = new BookAuthor();
                    bookAuthor.setBook(book);
                    bookAuthor.setAuthor(author);
                    allNewBookAuthor.add(bookAuthor);
                }

                //TODO:refactor to separate function
                List<Genre> allNewGenre = bookToGenreMapper
                                        .findOrCreateGenre(bookCsvDto, allGenresInDb);
                for (Genre genre : allNewGenre) {
                    BookGenre bookGenre = new BookGenre();
                    bookGenre.setBook(book);
                    bookGenre.setGenre(genre);
                    allBookGenres.add(bookGenre);
                }

                List<Format> allNewFormats = bookDtoToFormatMapper.mapBookToFormat(bookCsvDto, allFormatsInDb);
                for (Format format : allNewFormats) {
                    BookFormat bookFormat = new BookFormat();
                    bookFormat.setBook(book);
                    bookFormat.setFormat(format);
                    allNewBookFormat.add(bookFormat);
                }

                Map<String, Award> allAwards = bookDtoToAwardMapper.bookToAwardMapper(bookCsvDto, allAwardsInDb);

                for (Map.Entry<String, Award> entry : allAwards.entrySet()) {
                    BookAward bookAward = new BookAward();
                    bookAward.setAward(entry.getValue());
                    bookAward.setBook(book);
                    bookAward.setBookAward(entry.getKey());
                    allNewBookAward.add(bookAward);
                }

            }
            publisherRepository.saveAll(newPublishers);
            bookRepository.saveAll(allBooks);

            bookGenreRepository.saveAll(new ArrayList<>(allBookGenres));
            bookAuthorRepository.saveAll(allNewBookAuthor);
            bookFormatRepository.saveAll(allNewBookFormat);
            bookAwardRepository.saveAll(allNewBookAward);

            ratingByStarsRepository.saveAll(ratingByStars);
            charactersRepository.saveAll(allCharacters);

            allBookGenres.clear();
            allNewBookFormat.clear();
            allBooks.clear();
            allNewBookAuthor.clear();
            allNewBookFormat.clear();
            allNewBookAward.clear();
            ratingByStars.clear();
            allCharacters.clear();

            return bookCsvDtos.size();
        }

    public ResponseEntity<?> uploadAndSaveFile(MultipartFile file) throws UnableParseFile {
        try {
            List<BookCsvDto> csvDTos = csvReaderService.uploadBooks(file);
            if (csvDTos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("File already parsed NO new book\n");
            }
            return ResponseEntity.ok(saveBook(csvDTos) + " new  books were saved\n");
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new UnableParseFile("Unable to parse file due to "  + e.getCause());
        }
    }
}
