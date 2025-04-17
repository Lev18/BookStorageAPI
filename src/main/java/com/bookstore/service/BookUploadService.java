package com.bookstore.service;

import com.bookstore.entity.*;
import com.bookstore.repository.*;
import com.bookstore.dto.csvDto.BookCsvDto;
import com.bookstore.exception.UnableParseFile;
import com.bookstore.service.fileReader.CsvReaderService;
import com.bookstore.service.mapper.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
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

        List<Book> allBooks = Collections.synchronizedList(new ArrayList<>());
        List<BookGenre> allBookGenres = Collections.synchronizedList(new ArrayList<>());
        List<BookAuthor> allNewBookAuthor = Collections.synchronizedList(new ArrayList<>());
        List<BookFormat> allNewBookFormat = Collections.synchronizedList(new ArrayList<>());
        List<BookAward> allNewBookAward = Collections.synchronizedList(new ArrayList<>());
        List<BookPublisher> allNewBookPublishers = Collections.synchronizedList(new ArrayList<>());
        List<SeriesCharacter> allNewSeriesCharacter = Collections.synchronizedList(new ArrayList<>());
        List<BookSetting> allNewBookSettings = Collections.synchronizedList(new ArrayList<>());

        List<Publisher> newPublishers = Collections.synchronizedList(new ArrayList<>());
        List<BookCharacter> allNewBookCharacters = Collections.synchronizedList(new ArrayList<>());
        List<Genre> allNewGenres = Collections.synchronizedList(new ArrayList<>());
        List<Format> allNewFormats = Collections.synchronizedList(new ArrayList<>());
        List<Author> allNewAuthors = Collections.synchronizedList(new ArrayList<>());
        List<Award> allNewAwards = Collections.synchronizedList(new ArrayList<>());
        List<Setting> allNewSettings = Collections.synchronizedList(new ArrayList<>());
        List<Characters> allNewCharacters = Collections.synchronizedList(new ArrayList<>());
        List<Series> allNewSeries = Collections.synchronizedList(new ArrayList<>());

        Set<RatingByStars> ratingByStars = Collections.synchronizedSet(new HashSet<>());
        Set<String> uniqueIsbn = Collections.synchronizedSet(new HashSet<>(bookRepository.getAllISBNs()));


        //TODO: create data structure similar to LRUCache to avoid from memory overflow
        // search inside that cache, if cache miss request db
        // this is temporary solution it also can cause memory overflow


        Map<String, Author> allExistingAuthors = new ConcurrentHashMap<>();
        for (Author a : authorRepository.findAll()) {
            allExistingAuthors.put(a.getAuthorName().toLowerCase().trim(), a);
        }

        Map<String, Characters> allExistingCharacters = new ConcurrentHashMap<>();
        for (Characters a : charactersRepository.findAll()) {
            allExistingCharacters.put(a.getCharacterName().toLowerCase(), a);
        }

        Map<String, Award> allExistingAwards = new ConcurrentHashMap<>();
        for (Award a : awardsRepository.findAll()) {
            allExistingAwards.put(a.getName().toLowerCase(), a);
        }

        Map<String, Setting> allExistingSettings = new ConcurrentHashMap<>();
        for (Setting a : settingRepository.findAll()) {
            allExistingSettings.put(a.getSetting().toLowerCase(), a);
        }

        Map<String, Genre> allExistingGenres = new ConcurrentHashMap<>();
        for (Genre a : genreRepository.findAll()) {
            allExistingGenres.put(a.getGenreTitle().toLowerCase().trim(), a);
        }

        Map<String, Format> allFormatExists = new ConcurrentHashMap<>();
        for (Format a : formatRepository.findAll()) {
            allFormatExists.put(a.getFormat().toLowerCase(), a);
        }

        Map<String, Publisher> allPublisherExists = new HashMap<>();
        for (Publisher a : publisherRepository.findAll()) {
            allPublisherExists.put(a.getPublisherName().toLowerCase(), a);
        }

        Map<String, Series> allSeriesExists = new ConcurrentHashMap<>();
        for (Series a : seriesRepository.findAll()) {
            allSeriesExists.put(a.getSeries().toLowerCase(), a);
        }

        //renaming this variable name
        bookCsvDtos.parallelStream().forEach( bookCsvDto -> {
            try {
                if (!uniqueIsbn.add(bookCsvDto.getIsbn())) {
                    return;
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
                synchronized (allBooks) {
                    allBooks.add(book);
                }

                ratingByStars.addAll(book.getStars());

                List<Author> allAuthors = bookToAuthorMapper
                        .bookToAuthorMapper(bookCsvDto,
                                allExistingAuthors,
                                allNewAuthors);
                for (Author author : allAuthors) {
                    BookAuthor bookAuthor = new BookAuthor();
                    bookAuthor.setAuthor(author);
                    bookAuthor.setBook(book);
                    allNewBookAuthor.add(bookAuthor);
                }

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
            } catch (Exception e) {
                log.error("Error unable parsing DTO: {}", bookCsvDto, e);
                throw  new RuntimeException("Filed processing bookCsvDto");
            }
        });

        seriesRepository.saveAll(allNewSeries);
        bookRepository.saveAll(allBooks);
        genreRepository.saveAll(allNewGenres);
        charactersRepository.saveAll(allNewCharacters);
        formatRepository.saveAll(allNewFormats);
        awardsRepository.saveAll(allNewAwards);
        authorRepository.saveAll(allNewAuthors);
        settingRepository.saveAll(allNewSettings);
        publisherRepository.saveAll(newPublishers);

        bookGenreRepository.saveAll(allBookGenres);
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
    public int uploadAndSaveFile(MultipartFile file) throws UnableParseFile {
        List<BookCsvDto> csvDTos = csvReaderService.uploadBooks(file);

        return saveBook(csvDTos);
    }
}
