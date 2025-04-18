package com.bookstore.service;

import com.bookstore.entity.*;
import com.bookstore.mapper.*;
import com.bookstore.repository.*;
import com.bookstore.dto.csvDto.BookCsvDto;
import com.bookstore.exception.UnableParseFile;
import com.bookstore.service.fileReader.CsvReaderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.list.SynchronizedList;
import org.apache.commons.lang3.concurrent.Computable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component("appServiceImpl")
@AllArgsConstructor
public class BookUploadService {

    final int BATCH_SIZE = 1000;
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

    //Repositories
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
    @Async
    public int saveBook(List<BookCsvDto> bookCsvDtos) {

        List<Book> allBooks                         = Collections.synchronizedList(new ArrayList<>());
        List<BookGenre> allNewBookGenres            = Collections.synchronizedList(new ArrayList<>());
        List<BookAuthor> allNewBookAuthor           = Collections.synchronizedList(new ArrayList<>());
        List<BookFormat> allNewBookFormat           = Collections.synchronizedList(new ArrayList<>());
        List<BookAward> allNewBookAward             = Collections.synchronizedList(new ArrayList<>());
        List<BookPublisher> allNewBookPublishers    = Collections.synchronizedList(new ArrayList<>());
        List<SeriesCharacter> allNewSeriesCharacter = Collections.synchronizedList(new ArrayList<>());
        List<BookSetting> allNewBookSettings        = Collections.synchronizedList(new ArrayList<>());

        List<Publisher> allNewPublishers         = Collections.synchronizedList(new ArrayList<>());
        List<BookCharacter> allNewBookCharacters = Collections.synchronizedList(new ArrayList<>());
        List<Genre> allNewGenres                 = Collections.synchronizedList(new ArrayList<>());
        List<Format> allNewFormats               = Collections.synchronizedList(new ArrayList<>());
        List<Author> allNewAuthors               = Collections.synchronizedList(new ArrayList<>());
        List<Award> allNewAwards                 = Collections.synchronizedList(new ArrayList<>());
        List<Setting> allNewSettings             = Collections.synchronizedList(new ArrayList<>());
        List<Characters> allNewCharacters        = Collections.synchronizedList(new ArrayList<>());
        List<Series> allNewSeries                = Collections.synchronizedList(new ArrayList<>());
        Set<String> uniqueIsbn                   = Collections.synchronizedSet(new HashSet<>());

        Set<RatingByStars> ratingByStars = Collections.synchronizedSet(new HashSet<>());


        Map<String, Author> allExistingAuthors = authorRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(author -> author.getAuthorName()
                                .toLowerCase().trim(),
                        author -> author));


        Map<String, Characters> allExistingCharacters = charactersRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(characters -> characters.getCharacterName()
                                .toLowerCase().trim(),
                        characters -> characters));


        Map<String, Award> allExistingAwards = awardsRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(award -> award.getName()
                                .toLowerCase().trim(),
                        award -> award));


        Map<String, Setting> allExistingSettings = settingRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(setting -> setting.getSetting()
                                .toLowerCase().trim(),
                        setting -> setting));


        Map<String, Genre> allExistingGenres = genreRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(genre -> genre.getGenreTitle()
                                .toLowerCase().trim(),
                        genre -> genre));


        Map<String, Format> allFormatExists = formatRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(format -> format.getFormat()
                                .toLowerCase().trim(),
                        format -> format));

        Map<String, Publisher> allPublisherExists = publisherRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(publisher -> publisher.getPublisherName()
                                .toLowerCase().trim(),
                        publisher -> publisher));

        Map<String, Series> allSeriesExists = seriesRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(series -> series.getSeries()
                                .toLowerCase().trim(),
                        series -> series));

        List<List<BookCsvDto>> batches = new ArrayList<>();
        for (int i = 0; i < bookCsvDtos.size(); i += BATCH_SIZE) {
            batches.add(bookCsvDtos.subList(i, Math.min(i + BATCH_SIZE, bookCsvDtos.size())));
        }


        //renaming this variable name
        List<CompletableFuture<Void>> futures = batches.stream().map(
                batch -> CompletableFuture.runAsync(() -> {
                    for (BookCsvDto bookCsvDto : batch) {
                        try {
                            if (!uniqueIsbn.add(bookCsvDto.getIsbn())) continue;

                            Book book = bookDtoToBookDBMapper.bookDtoToBookMapper(bookCsvDto);
                            Series series = bookDtoToSeriesMapper.mapBookToSeries(bookCsvDto,
                                                                        allSeriesExists, allNewSeries);
                            if (series != null) book.setSeries(series);

                            allBooks.add(book);
                            ratingByStars.addAll(book.getStars());

                            allNewBookAuthor.addAll(bookToAuthorMapper.bookToAuthorMapper(bookCsvDto,
                                    allExistingAuthors, allNewAuthors).stream()
                                    .map(author -> new BookAuthor(book, author)).toList());

                            allNewBookGenres.addAll(bookToGenreMapper.findOrCreateGenre(bookCsvDto,
                                    allExistingGenres, allNewGenres).stream()
                                    .map(genre -> new BookGenre(book, genre)).toList());

                            allNewBookFormat.addAll(bookDtoToFormatMapper.mapBookToFormat(bookCsvDto,
                                    allFormatExists, allNewFormats).stream()
                                    .map(format -> new BookFormat(book, format)).toList());

                            bookDtoToAwardMapper.bookToAwardMapper(bookCsvDto,
                                    allExistingAwards, allNewAwards).forEach(
                                    (awardType, award) -> allNewBookAward
                                            .add(new BookAward(book, award, awardType))
                            );

                            for (Characters c : characterMapper.mapBookDtoToCharacter(bookCsvDto,
                                    allExistingCharacters, allNewCharacters)) {
                                allNewBookCharacters.add(new BookCharacter(book, c));
                                if (series != null) {
                                    allNewSeriesCharacter.add(new SeriesCharacter(series, c));
                                }
                            }

                            allNewBookSettings.addAll(bookDtoToSettingMapper.mapBookDtoToSetting(bookCsvDto,
                                    allExistingSettings, allNewSettings).stream()
                                    .map(setting -> new BookSetting(book, setting)).toList());

                            allNewBookPublishers.addAll(bookDtoToPublisherMapper.bookToPublisherMapper(bookCsvDto,
                                    allPublisherExists, allNewPublishers).stream()
                                    .map(publisher -> new BookPublisher(book, publisher)).toList());
                        } catch (Exception e) {
                            log.error("Error processing book DTO: {}", bookCsvDto, e);
                        }
                    }

                })
        ).toList();

        futures.forEach(CompletableFuture::join);

        // Persist results
        if (!allNewSeries.isEmpty()) seriesRepository.saveAll(allNewSeries);
        if (!allBooks.isEmpty()) bookRepository.saveAll(allBooks);
        if (!allNewGenres.isEmpty()) genreRepository.saveAll(allNewGenres);
        if (!allNewAuthors.isEmpty()) authorRepository.saveAll(allNewAuthors);
        if (!allNewFormats.isEmpty()) formatRepository.saveAll(allNewFormats);
        if (!allNewPublishers.isEmpty()) publisherRepository.saveAll(allNewPublishers);
        if (!allNewAwards.isEmpty()) awardsRepository.saveAll(allNewAwards);
        if (!allNewCharacters.isEmpty()) charactersRepository.saveAll(allNewCharacters);
        if (!allNewSettings.isEmpty()) settingRepository.saveAll(allNewSettings);

        if (!allNewBookGenres.isEmpty()) bookGenreRepository.saveAll(allNewBookGenres);
        if (!allNewBookAuthor.isEmpty()) bookAuthorRepository.saveAll(allNewBookAuthor);
        if (!allNewBookFormat.isEmpty()) bookFormatRepository.saveAll(allNewBookFormat);
        if (!allNewBookAward.isEmpty()) bookAwardRepository.saveAll(allNewBookAward);
        if (!allNewBookCharacters.isEmpty()) bookCharacterRepository.saveAll(allNewBookCharacters);
        if (!allNewBookSettings.isEmpty()) bookSettingRepository.saveAll(allNewBookSettings);
        if (!allNewBookPublishers.isEmpty()) bookPublisherRepository.saveAll(allNewBookPublishers);
        if (!allNewSeriesCharacter.isEmpty()) seriesCharacterRepository.saveAll(allNewSeriesCharacter);
        if (!ratingByStars.isEmpty()) ratingByStarsRepository.saveAll(ratingByStars);

        return allBooks.size();
    }

    // ResponseEntity<?> move to controller
    public int uploadAndSaveFile(MultipartFile file) throws UnableParseFile {
        List<BookCsvDto> csvDTos = csvReaderService.uploadBooks(file);

        return saveBook(csvDTos);
    }
}
