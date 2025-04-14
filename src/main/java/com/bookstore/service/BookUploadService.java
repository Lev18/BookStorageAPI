package com.bookstore.service;

import com.bookstore.entity.*;
import com.bookstore.repository.*;
import com.bookstore.service.dto.BookCsvDto;
import com.bookstore.service.exception.UnableParseFile;
import com.bookstore.service.fileReader.CsvReaderService;
import com.bookstore.service.mapper.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Component("appServiceImpl")
@AllArgsConstructor
public class BookUploadService {
    private final CsvReaderService csvReaderService;
    private final BookDtoToBookDBMapper bookDtoToBookDBMapper;
    // TODO: collect into BookMapperService class
    private final BookToGenreMapper bookToGenreMapper;
    private final BookToAuthorMapper bookToAuthorMapper;
    private final BookDtoToFormatMapper bookDtoToFormatMapper;
    private final BookDtoToAwardMapper bookDtoToAwardMapper;
    private final BookToPublisherMapper bookDtoToPublisherMapper;
    private final BookDtoToCharacterMapper characterMapper;
    private final BookDtoToSettingMapper bookDtoToSettingMapper;
    private final BookDtoToSeriesMapper bookDtoToSeriesMapper;


    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final AwardsRepository awardsRepository;
    private final BookAwardRepository bookAwardRepository;
    private final BookCharactersRepository bookCharacterRepository;
    private final BookGenreRepository bookGenreRepository;
    private final GenreRepository genreRepository;
    private final FormatRepository formatRepository;
    private final RatingByStarsRepository ratingByStarsRepository;
    private final SeriesRepository seriesRepository;

    private final BookFormatRepository bookFormatRepository;
    private final CharactersRepository charactersRepository;
    private final PublisherRepository publisherRepository;
    private final SettingRepository settingRepository;
    private final BookSettingRepository bookSettingRepository;
    private final BookPublisherRepository bookPublisherRepository;
    private final SeriesCharacterRepository seriesCharacterRepository;


    @Transactional
    public int saveBook(List<BookCsvDto> bookCsvDtos) {

        List<Book> allBooks = new ArrayList<>();
        List<BookGenre> allBookGenres = new ArrayList<>();
        List<BookAuthor> allNewBookAuthor = new ArrayList<>();
        List<BookFormat> allNewBookFormat = new ArrayList<>();
        List<BookAward> allNewBookAward = new ArrayList<>();
        List<BookPublisher> allNewBookPublishers = new ArrayList<>();
        List<SeriesCharacter> allNewSeriesCharacter = new ArrayList<>();
        List<BookSetting> allNewBookSettings = new ArrayList<>();

        List<Publisher> newPublishers = new ArrayList<>();
        List<BookCharacter> allNewBookCharacters = new ArrayList<>();
        List<Genre> allNewGenres = new ArrayList<>();
        List<Format> allNewFormats = new ArrayList<>();
        List<Author> allNewAuthors = new ArrayList<>();
        List<Award> allNewAwards = new ArrayList<>();
        List<Setting> allNewSettings = new ArrayList<>();
        List<Characters> allNewCharacters = new ArrayList<>();
        List<Series> allNewSeries = new ArrayList<>();

        Set<RatingByStars> ratingByStars = new HashSet<>();
        Set<String> uniqueIsbn = new HashSet<>(bookRepository.getAllISBNs());


        //TODO: create data structure similar to LRUCache to avoid from memory overflow
        // search inside that cache, if cache miss request db
        // this is temporary solution it also can cause memory overflow


        Map<String, Author> allExistingAuthors = new HashMap<>();
        for (Author a : authorRepository.findAll()) {
            allExistingAuthors.put(a.getAuthorName().toLowerCase(), a);
        }

        Map<String, Characters> allExistingCharacters = new HashMap<>();
        for (Characters a : charactersRepository.findAll()) {
            allExistingCharacters.put(a.getCharacterName().toLowerCase(), a);
        }

        Map<String, Award> allExistingAwards = new HashMap<>();
        for (Award a : awardsRepository.findAll()) {
            allExistingAwards.put(a.getName().toLowerCase(), a);
        }

        Map<String, Setting> allExistingSettings = new HashMap<>();
        for (Setting a : settingRepository.findAll()) {
            allExistingSettings.put(a.getSetting().toLowerCase(), a);
        }

        Map<String, Genre> allExistingGenres = new HashMap<>();
        for (Genre a : genreRepository.findAll()) {
            allExistingGenres.put(a.getGenreTitle().toLowerCase(), a);
        }

        Map<String, Format> allFormatExists = new HashMap<>();
        for (Format a : formatRepository.findAll()) {
            allFormatExists.put(a.getFormat().toLowerCase(), a);
        }

        Map<String, Publisher> allPublisherExists = new HashMap<>();
        for (Publisher a : publisherRepository.findAll()) {
            allPublisherExists.put(a.getPublisherName().toLowerCase(), a);
        }

        Map<String, Series> allSeriesExists = new HashMap<>();
        for (Series a : seriesRepository.findAll()) {
            allSeriesExists.put(a.getSeries().toLowerCase(), a);
        }

        //renaming this variable name

        for (BookCsvDto bookCsvDto : bookCsvDtos) {
            if (uniqueIsbn.contains(bookCsvDto.getIsbn())) {
                // System.out.println("[INFO:] " + "\"\u001B[4m" + bookCsvDto.getTitle() + "\u001B[0m\" already exist or incorrect isbn series");
                continue;
            }



            Book book = bookDtoToBookDBMapper.bookDtoToBookMapper(bookCsvDto);
            Series newSeries = bookDtoToSeriesMapper
                                        .mapBookToSeries(bookCsvDto,
                                                allSeriesExists,
                                                allNewSeries);
            //TODO: separate mapper logic from save
            // example
//            bookMapperService.mapBookToAuthor(bookCsvDto,
//                    allExistingAuthors,
//                    allNewAuthors,
//                    allNewBookAuthor);
//
            if (newSeries != null) {
                book.setSeries(newSeries);
            }

            allBooks.add(book);

            ratingByStars.addAll(book.getStars());
            uniqueIsbn.add(bookCsvDto.getIsbn());


            List<Author> allAuthors = bookToAuthorMapper
                    .bookToAuthorMapper(bookCsvDto,
                            allExistingAuthors,
                            allNewAuthors);
            for (Author author : allAuthors) {
                BookAuthor bookAuthor = new BookAuthor();
                bookAuthor.setBook(book);
                bookAuthor.setAuthor(author);
                allNewBookAuthor.add(bookAuthor);
            }

            //TODO:refactor to separate function
            List<Genre> allGenre = bookToGenreMapper
                    .findOrCreateGenre(bookCsvDto,
                            allExistingGenres,
                            allNewGenres);
            for (Genre genre : allGenre) {
                BookGenre bookGenre = new BookGenre();
                bookGenre.setBook(book);
                bookGenre.setGenre(genre);
                allBookGenres.add(bookGenre);
            }

            List<Format> allFormats = bookDtoToFormatMapper
                                .mapBookToFormat(bookCsvDto,
                                        allFormatExists,
                                        allNewFormats);
            for (Format format : allFormats) {
                BookFormat bookFormat = new BookFormat();
                bookFormat.setBook(book);
                bookFormat.setFormat(format);
                allNewBookFormat.add(bookFormat);
            }

            Map<String, Award> allAwards = bookDtoToAwardMapper
                                .bookToAwardMapper(bookCsvDto,
                                    allExistingAwards,
                                    allNewAwards);
            for (Map.Entry<String, Award> entry : allAwards.entrySet()) {
                BookAward bookAward = new BookAward();
                bookAward.setAward(entry.getValue());
                bookAward.setBook(book);
                bookAward.setBookAward(entry.getKey());
                allNewBookAward.add(bookAward);
            }

            List<Characters> allCharacters = characterMapper
                        .mapBookDtoToCharacter(bookCsvDto,
                                    allExistingCharacters,
                                    allNewCharacters);
            for (Characters character : allCharacters) {
                BookCharacter bookCharacter = new BookCharacter();
                bookCharacter.setBook(book);
                bookCharacter.setCharacter(character);
                allNewBookCharacters.add(bookCharacter);

                if (newSeries != null) {
                    SeriesCharacter seriesCharacter = new SeriesCharacter();
                    seriesCharacter.setSeries(newSeries);
                    seriesCharacter.setCharacter(character);
                    allNewSeriesCharacter.add(seriesCharacter);
                }
            }

            List<Setting> allSettings = bookDtoToSettingMapper
                            .mapBookDtoToSetting(bookCsvDto,
                                    allExistingSettings,
                                    allNewSettings);
            for (Setting setting : allSettings) {
                BookSetting bookSetting = new BookSetting();
                bookSetting.setBook(book);
                bookSetting.setSetting(setting);
                allNewBookSettings.add(bookSetting);
            }

            List<Publisher> allPublishers = bookDtoToPublisherMapper
                                    .bookToPublisherMapper(bookCsvDto,
                                                        allPublisherExists,
                                                        newPublishers);
            for (Publisher publisher : allPublishers) {
                BookPublisher bookPublisher = new BookPublisher();
                bookPublisher.setBook(book);
                bookPublisher.setPublisher(publisher);
                allNewBookPublishers.add(bookPublisher);
            }
        }

        seriesRepository.saveAll(allNewSeries);
        bookRepository.saveAll(allBooks);
        genreRepository.saveAll(allNewGenres);
        charactersRepository.saveAll(allNewCharacters);
        formatRepository.saveAll(allNewFormats);
        authorRepository.saveAll(allNewAuthors);
        awardsRepository.saveAll(allNewAwards);
        settingRepository.saveAll(allNewSettings);
        publisherRepository.saveAll(newPublishers);

        bookGenreRepository.saveAll(new ArrayList<>(allBookGenres));
        bookAuthorRepository.saveAll(allNewBookAuthor);
        bookFormatRepository.saveAll(allNewBookFormat);
        bookAwardRepository.saveAll(allNewBookAward);
        bookCharacterRepository.saveAll(allNewBookCharacters);
        bookSettingRepository.saveAll(allNewBookSettings);
        bookPublisherRepository.saveAll(allNewBookPublishers);
        seriesCharacterRepository.saveAll(allNewSeriesCharacter);

        ratingByStarsRepository.saveAll(ratingByStars);


        return allBooks.size();
    }

    // ResponseEntity<?> move to controller
    public ResponseEntity<?> uploadAndSaveFile(MultipartFile file) throws UnableParseFile {
        List<BookCsvDto> csvDTos = csvReaderService.uploadBooks(file);
        if (csvDTos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("File already parsed NO new book\n");
        }
        return ResponseEntity.ok(saveBook(csvDTos) + " new  books were saved\n");
    }
}
