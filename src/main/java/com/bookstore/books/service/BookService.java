package com.bookstore.books.service;

import com.bookstore.books.criteria.BookSearchCriteria;
import com.bookstore.books.dto.csvDto.BookCsvDto;
import com.bookstore.books.dto.requestDto.AwardDto;
import com.bookstore.books.dto.requestDto.BookRequestDto;
import com.bookstore.books.dto.responseDto.*;
import com.bookstore.books.entity.*;
import com.bookstore.books.enums.FileDownloadStatus;
import com.bookstore.books.exception.BookAlreadyExistException;
import com.bookstore.books.exception.ImageNotFoundException;
import com.bookstore.books.exception.UnableParseFileException;
import com.bookstore.books.mapper.*;
import com.bookstore.books.repository.*;
import com.bookstore.books.service.fileReader.CsvReaderService;
import com.bookstore.books.utils.imageLoader.ImageLoaderService;
import com.bookstore.books.utils.imageLoader.ImageResizeService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookService {
    final int BATCH_SIZE = 2000;

    private final AtomicInteger imageCtr = new AtomicInteger();

    private final ImageLoaderService imageLoader;
    private final EntityManager entityManager;

    private final BookRepository bookRepository;
    private final AwardsRepository awardsRepository;
    private final BookAwardRepository bookAwardRepository;
    private final GenreRepository genreRepository;
    private final RatingByStarsRepository ratingByStarsRepository;

    // TODO: this is temporary solution
    private final ImageLoaderService imageLoaderService;
    private final ImageResizeService imageResizeService;


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
    private final BookDtoToImageMapper bookDtoToImageMapper;
    private final BookToInfoDtoMapper bookToInfoDtoMapper;

    //Repositories
    private final AuthorRepository authorRepository;
    private final BookAuthorRepository bookAuthorRepository;
    private final BookCharactersRepository bookCharacterRepository;
    private final BookGenreRepository bookGenreRepository;
    private final FormatRepository formatRepository;
    private final SeriesRepository seriesRepository;
    private final FileRepository fileRepository;

    private final BookFormatRepository bookFormatRepository;
    private final CharactersRepository charactersRepository;
    private final PublisherRepository publisherRepository;
    private final SettingRepository settingRepository;
    private final BookSettingRepository bookSettingRepository;
    private final BookPublisherRepository bookPublisherRepository;
    private final SeriesCharacterRepository seriesCharacterRepository;


    public Book updateBookRating(String bookIsbn, Integer newRate) {
        Book book = bookRepository.findBookByIsbn(bookIsbn);
        List<RatingByStars> ratingByStars = ratingByStarsRepository.findAllByBook(book);
        for (RatingByStars rating : ratingByStars) {
            if (newRate == rating.getGrade()) {
                Integer oldRating = rating.getRatingStars();
                rating.setRatingStars(++oldRating);
            }
        }
        book.setNumRatingByStars(ratingByStars);
        book.setStars(ratingByStars);

        bookRepository.save(book);
        ratingByStarsRepository.saveAll(ratingByStars);

        return book;
    }

    @SneakyThrows
    public byte[] getBookImg(String bookIsbn) {
        String bookImgUrl = bookRepository.getUrlById(bookIsbn);
        String[] urlComponents = bookImgUrl.split("/");
        String img = urlComponents[urlComponents.length - 1];
        String directory = urlComponents[urlComponents.length - 2];

        // move to application properties
        // how get original or resized
        // with parameter enum
        // truck uploaded images keep also download status in db keep images
        Path path = Paths.get("/home/levon/Workspace/ImagesBook/resized/" + directory + "/" + img);
        try (InputStream in = path.toUri().toURL()
                .openStream()) {

            return in.readAllBytes();
        } catch (FileNotFoundException e) {
            throw new ImageNotFoundException("Image not found, no such file or directory");
        }
    }

    public List<GenreInfoDTO> getAllBooksByGenre(String genre) {
        TypedQuery<GenreInfoDTO> query
                = entityManager.createQuery(
                "select new GenreInfoDTO(b.title, g.genreTitle, b.isbn) " +
                        "from Book b " +
                        "left join b.genres bg" +
                        "left join Genre g on g.id = bg.genre.id " +
                        "where genreTitle = :genreTitle " +
                        "order by bg.id ",
                GenreInfoDTO.class
        );
        query.setParameter("genreTitle", genre);
        query.setMaxResults(100);

        return query.getResultList();
    }

    public BookInfoDTO getBookByISBN(String id) {
        Book book = bookRepository.findBookWithAttributes(id);
        if (book == null) {
            return null;
        }

        Set<String> genres = book.getGenres()
                .stream()
                .map(bg -> bg.getGenre().getName())
                .collect(Collectors.toSet());

        Set<String> awards = book.getAwards()
                .stream()
                .map(BookAward::getBookAward)
                .collect(Collectors.toSet());

        Set<String> characters = book.getCharacters()
                .stream()
                .map(bookCharacter -> bookCharacter.getCharacter()
                        .getName())
                .collect(Collectors.toSet());

        Double rating = countBookRating(book);
        Set<String> author = book.getAuthor()
                .stream()
                .map(bookAuthor -> bookAuthor.getAuthor().getName())
                .collect(Collectors.toSet());

        Set<String> settings = book.getBookSettings()
                .stream()
                .map(bookSetting -> bookSetting.getSetting().getSetting())
                .collect(Collectors.toSet());

        return new BookInfoDTO(book.getId(),
                book.getTitle(), author,
                book.getIsbn(),
                book.getDescription(),
                book.getPages(),
                book.getPublishDate(),
                book.getLanguage().toString(),
                awards,
                characters,
                genres,
                settings,
                rating);
    }

    private Double round(double value, int place) {
        if (place < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bigDecimal = new BigDecimal(Double.toString(value));
        bigDecimal = bigDecimal.setScale(place, RoundingMode.HALF_EVEN);

        return bigDecimal.doubleValue();
    }

    private Double countBookRating(Book book) {
        List<RatingByStars> ratingByStars = ratingByStarsRepository.findAllByBook(book);
        int rateCount = 0;
        int stars = 0;
        double wholeRate = 0f;
        for (RatingByStars rating : ratingByStars) {
            rateCount += rating.getGrade() * rating.getRatingStars();
            stars += rating.getRatingStars();
        }

        try {
            wholeRate = (double) rateCount / stars;
        } catch (ArithmeticException e) {
            System.out.println(
                    "Divided by zero operation cannot possible");
        }
        return round(wholeRate, 2);
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
        return delCandidate;
    }

    public Genre deleteGenre(String genreName) {
        Genre genre = genreRepository.findByName(genreName);
        genreRepository.deleteBookGenresByGenreId(genre.getId());
        genreRepository.delete(genre);
        return genre;
    }

    public String addNewAward(String bookIsbn, AwardDto newAward) {
        Book book = bookRepository.findBookByIsbn(bookIsbn);
        if (book != null) {
            Award award = awardsRepository.findByName(newAward.getAward());
            if (award == null) {
                award = new Award(newAward.getAward());
                awardsRepository.save(award);
            }
            bookRepository.save(book);
            BookAward bookAward = new BookAward(book, award,
                    newAward.getAward() + '(' + newAward.getAwardYear() + ')');
            bookAwardRepository.save(bookAward);
            return book.getTitle();
        }
        return null;
    }

    public BookInfoDTO insertNewBook(BookRequestDto bookRequestDto) throws BookAlreadyExistException {
        if (bookRepository.findBookByIsbn(bookRequestDto.getIsbn()) != null) {
            throw new BookAlreadyExistException("The current book already exist ");
        }

        final BookInfoDTO bookInfoDTO = new BookInfoDTO();

        List<BookAward> allNewBookAward = new ArrayList<>();
        List<SeriesCharacter> allNewSeriesCharacter = new ArrayList<>();

        List<Publisher> allNewPublishers = new ArrayList<>();
        List<BookCharacter> allNewBookCharacters = new ArrayList<>();
        List<Genre> allNewGenres = new ArrayList<>();
        List<Format> allNewFormats = new ArrayList<>();
        List<Author> allNewAuthors = new ArrayList<>();
        List<Award> allNewAwards = new ArrayList<>();
        List<Setting> allNewSettings = new ArrayList<>();
        List<Characters> allNewCharacters = new ArrayList<>();
        List<Series> allNewSeries = new ArrayList<>();

        Map<String, Author> allExistingAuthors = authorRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(author -> author.getName()
                                .toLowerCase().trim(),
                        author -> author));


        Map<String, Characters> allExistingCharacters = charactersRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(characters -> characters.getName()
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
                .collect(Collectors.toConcurrentMap(genre -> genre.getName()
                                .toLowerCase().trim(),
                        genre -> genre));


        Map<String, Format> allFormatExists = formatRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(format -> format.getFormat()
                                .toLowerCase().trim(),
                        format -> format));

        Map<String, Publisher> allPublisherExists = publisherRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(publisher -> publisher.getName()
                                .toLowerCase().trim(),
                        publisher -> publisher));

        Map<String, Series> allSeriesExists = seriesRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(series -> series.getSeries()
                                .toLowerCase().trim(),
                        series -> series));

        Book book = bookDtoToBookDBMapper.bookDtoToBookMapper(bookRequestDto);

        Series series = bookDtoToSeriesMapper.mapBookToSeries(bookRequestDto.getSeries(),
                allSeriesExists, allNewSeries);
        if (series != null) book.setSeries(series);

        List<BookAuthor> allNewBookAuthor = new ArrayList<>(bookToAuthorMapper.mapBookToAuthor(
                        bookRequestDto,
                        BookRequestDto::getAuthor,
                        allExistingAuthors,
                        allNewAuthors).stream()
                .map(author -> new BookAuthor(book, author)).toList());

        List<BookGenre> allNewBookGenres = new ArrayList<>(bookToGenreMapper.mapBookToGenre(
                        bookRequestDto.getGenres(),
                        allExistingGenres,
                        allNewGenres).stream()
                .map(genre -> new BookGenre(book, genre)).toList());

        List<BookFormat> allNewBookFormat = new ArrayList<>(bookDtoToFormatMapper.mapBookToFormat(
                        bookRequestDto,
                        BookRequestDto::getFormats,
                        allFormatExists,
                        allNewFormats).stream()
                .map(format -> new BookFormat(book, format)).toList());

        bookDtoToAwardMapper.bookToAwardMapper(bookRequestDto.getAwards(),
                allExistingAwards, allNewAwards).forEach(
                (awardType, award) -> allNewBookAward
                        .add(new BookAward(book, award, awardType))
        );

        for (Characters c : characterMapper.mapBookDtoToCharacter(
                bookRequestDto.getCharacters(),
                allExistingCharacters,
                allNewCharacters)) {
            allNewBookCharacters.add(new BookCharacter(book, c));
            if (series != null) {
                allNewSeriesCharacter.add(new SeriesCharacter(series, c));
            }
        }

        List<BookSetting> allNewBookSettings = new ArrayList<>(bookDtoToSettingMapper.mapBookDtoToSetting(
                        bookRequestDto.getSettings(),
                        allExistingSettings,
                        allNewSettings).stream()
                .map(setting -> new BookSetting(book, setting)).toList());

        List<BookPublisher> allNewBookPublishers = new ArrayList<>(bookDtoToPublisherMapper.mapBookToPublisher(
                        bookRequestDto,
                        BookRequestDto::getPublishers,
                        allPublisherExists,
                        allNewPublishers).stream()
                .map(publisher -> new BookPublisher(book, publisher)).toList());


        seriesRepository.saveAll(allNewSeries);

        bookRepository.save(book);

        genreRepository.saveAll(allNewGenres);
        authorRepository.saveAll(allNewAuthors);
        formatRepository.saveAll(allNewFormats);
        publisherRepository.saveAll(allNewPublishers);
        awardsRepository.saveAll(allNewAwards);
        charactersRepository.saveAll(allNewCharacters);
        settingRepository.saveAll(allNewSettings);

        bookGenreRepository.saveAll(allNewBookGenres);
        bookAuthorRepository.saveAll(allNewBookAuthor);
        bookFormatRepository.saveAll(allNewBookFormat);
        bookAwardRepository.saveAll(allNewBookAward);
        bookCharacterRepository.saveAll(allNewBookCharacters);
        bookSettingRepository.saveAll(allNewBookSettings);
        bookPublisherRepository.saveAll(allNewBookPublishers);
        seriesCharacterRepository.saveAll(allNewSeriesCharacter);

        return bookToInfoDtoMapper.mapBookRequestToInfo(book.getId(), bookRequestDto);
    }

    // TODO: use string_aggregate method in sql query
    public PageResponseDto<BookResponseDto> findAllByCriteria(BookSearchCriteria bookSearchCriteria) {
        // request db for getting books
        Page<BookFlatRecord> books = bookRepository.findAll(bookSearchCriteria, bookSearchCriteria.buildPageRequest());
        // mapping them into BookResponse
        Map<Long, BookResponseDto> bookMap = getLongBookResponseDtoMap(books);

        List<BookResponseDto> groupedDtos = new ArrayList<>(bookMap.values().stream().sorted(
                (bkRs1, bkRs2) -> bkRs1.getId().compareTo(bkRs2.getId())
        ).toList());

        return PageResponseDto.from(new PageImpl<>(groupedDtos,
                books.getPageable(),
                books.getTotalElements()));
    }

    private Map<Long, BookResponseDto> getLongBookResponseDtoMap(Page<BookFlatRecord> books) {
        Map<Long, BookResponseDto> bookMap = new HashMap<>();
        for (BookFlatRecord bookFlatRecord : books) {
            BookResponseDto bookResponseDto = bookMap.computeIfAbsent(bookFlatRecord.id(),
                    id -> new BookResponseDto(bookFlatRecord.id(),
                            bookFlatRecord.title(),
                            bookFlatRecord.series(),
                            bookFlatRecord.publishDate(),
                            bookFlatRecord.rating()
                    ));
            bookResponseDto.addAuthor(bookFlatRecord.author());
            bookResponseDto.addAward(bookFlatRecord.award());
            bookResponseDto.addCharacter(bookFlatRecord.character());
            bookResponseDto.addGenre(bookFlatRecord.genre());
            bookResponseDto.addPublisher(bookFlatRecord.publisher());
        }
        return bookMap;
    }


    @Transactional
    public int saveBook(List<BookCsvDto> bookCsvDtos) {

        List<Book> allBooks = Collections.synchronizedList(new ArrayList<>());
        List<BookGenre> allNewBookGenres = Collections.synchronizedList(new ArrayList<>());
        List<BookAuthor> allNewBookAuthor = Collections.synchronizedList(new ArrayList<>());
        List<BookFormat> allNewBookFormat = Collections.synchronizedList(new ArrayList<>());
        List<BookAward> allNewBookAward = Collections.synchronizedList(new ArrayList<>());
        List<BookPublisher> allNewBookPublishers = Collections.synchronizedList(new ArrayList<>());
        List<SeriesCharacter> allNewSeriesCharacter = Collections.synchronizedList(new ArrayList<>());
        List<BookSetting> allNewBookSettings = Collections.synchronizedList(new ArrayList<>());

        List<Publisher> allNewPublishers = Collections.synchronizedList(new ArrayList<>());
        List<BookCharacter> allNewBookCharacters = Collections.synchronizedList(new ArrayList<>());
        List<Genre> allNewGenres = Collections.synchronizedList(new ArrayList<>());
        List<Format> allNewFormats = Collections.synchronizedList(new ArrayList<>());
        List<Author> allNewAuthors = Collections.synchronizedList(new ArrayList<>());
        List<Award> allNewAwards = Collections.synchronizedList(new ArrayList<>());
        List<Setting> allNewSettings = Collections.synchronizedList(new ArrayList<>());
        List<Characters> allNewCharacters = Collections.synchronizedList(new ArrayList<>());
        List<Series> allNewSeries = Collections.synchronizedList(new ArrayList<>());
        List<FileInfo> allNewImages = Collections.synchronizedList(new ArrayList<>());

        Set<String> uniqueIsbn = Collections.synchronizedSet(bookRepository.getAllISBNs());

        Set<RatingByStars> ratingByStars = Collections.synchronizedSet(new HashSet<>());

        Map<String, Author> allExistingAuthors = authorRepository.findAll().stream()
                .filter(author -> author != null && author.getName() != null)
                .collect(Collectors.toConcurrentMap(author -> author.getName()
                                .toLowerCase().trim(),
                        author -> author));


        Map<String, Characters> allExistingCharacters = charactersRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(characters -> characters.getName()
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
                .collect(Collectors.toConcurrentMap(genre -> genre.getName()
                                .toLowerCase().trim(),
                        genre -> genre));


        Map<String, Format> allFormatExists = formatRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(format -> format.getFormat()
                                .toLowerCase().trim(),
                        format -> format));

        Map<String, Publisher> allPublisherExists = publisherRepository.findAll().stream()
                .collect(Collectors.toConcurrentMap(publisher -> publisher.getName()
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
        ExecutorService executor = Executors.newFixedThreadPool(22);
        //renaming this variable name
        List<CompletableFuture<Void>> futures = batches.stream().map(
                batch -> CompletableFuture.runAsync(() -> {
                    for (BookCsvDto bookCsvDto : batch) {
                        try {
                            if (!uniqueIsbn.add(bookCsvDto.getIsbn())) continue;

                            Book book = bookDtoToBookDBMapper.bookDtoToBookMapper(bookCsvDto);
                            Series series = bookDtoToSeriesMapper.mapBookToSeries(bookCsvDto.getSeries(),
                                    allSeriesExists, allNewSeries);
                            // int currentCount = ;
                            bookDtoToImageMapper.mapImageFileFromBookDto(book,
                                    bookCsvDto.getCoverImg(),
                                    allNewImages);

                            if (series != null) book.setSeries(series);

                            allBooks.add(book);
                            ratingByStars.addAll(book.getStars());

                            allNewBookAuthor.addAll(bookToAuthorMapper.mapBookToAuthor(
                                            bookCsvDto,
                                            dto -> Arrays.stream(dto.getAuthor()
                                                    .split(", "))
                                                    .map(String::trim)
                                                    .toList(),
                                            allExistingAuthors,
                                            allNewAuthors).stream()
                                    .map(author -> new BookAuthor(book, author)).toList());

                            allNewBookGenres.addAll(bookToGenreMapper.mapBookToGenre(
                                            bookCsvDto.getGenres(),
                                            allExistingGenres,
                                            allNewGenres).stream()
                                    .map(genre -> new BookGenre(book, genre)).toList());

                            allNewBookFormat.addAll(bookDtoToFormatMapper.mapBookToFormat(
                                            bookCsvDto,
                                            dto -> Arrays.stream(dto.getBookFormat()
                                                            .split(", "))
                                                    .map(String::trim)
                                                    .toList(),
                                            allFormatExists,
                                            allNewFormats).stream()
                                    .map(format -> new BookFormat(book, format)).toList());

                            bookDtoToAwardMapper.bookToAwardMapper(bookCsvDto.getAwards(),
                                    allExistingAwards,
                                    allNewAwards).forEach(
                                    (awardType, award) -> allNewBookAward
                                            .add(new BookAward(book, award, awardType))
                            );

                            for (Characters c : characterMapper.mapBookDtoToCharacter(
                                    bookCsvDto.getCharacters(),
                                    allExistingCharacters,
                                    allNewCharacters)) {
                                allNewBookCharacters.add(new BookCharacter(book, c));
                                if (series != null) {
                                    allNewSeriesCharacter.add(new SeriesCharacter(series, c));
                                }
                            }
                            allNewBookSettings.addAll(bookDtoToSettingMapper.mapBookDtoToSetting(
                                            bookCsvDto.getSetting(),
                                            allExistingSettings,
                                            allNewSettings).stream()
                                    .map(setting -> new BookSetting(book, setting)).toList());

                            allNewBookPublishers.addAll(bookDtoToPublisherMapper.mapBookToPublisher(
                                            bookCsvDto,
                                            dto -> Arrays.stream(dto.getPublisher().split("/"))
                                                    .map(String::trim)
                                                    .toList(),
                                            allPublisherExists,
                                            allNewPublishers).stream()
                                    .map(publisher -> new BookPublisher(book, publisher)).toList());


                        } catch (Exception e) {
                            log.error("Error processing book DTO: {}", bookCsvDto, e);
                        }
                    }
                }, executor)
        ).toList();

        futures.forEach(CompletableFuture::join);
        seriesRepository.saveAll(allNewSeries);

        bookRepository.saveAll(allBooks);

        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> fileRepository.saveAll(allNewImages)),
                CompletableFuture.runAsync(() -> genreRepository.saveAll(allNewGenres)),
                CompletableFuture.runAsync(() -> authorRepository.saveAll(allNewAuthors)),
                CompletableFuture.runAsync(() -> formatRepository.saveAll(allNewFormats)),
                CompletableFuture.runAsync(() -> publisherRepository.saveAll(allNewPublishers)),
                CompletableFuture.runAsync(() -> awardsRepository.saveAll(allNewAwards)),
                CompletableFuture.runAsync(() -> charactersRepository.saveAll(allNewCharacters)),
                CompletableFuture.runAsync(() -> settingRepository.saveAll(allNewSettings))
        ).join();
        CompletableFuture.allOf(
                CompletableFuture.runAsync(() -> bookGenreRepository.saveAll(allNewBookGenres)),
                CompletableFuture.runAsync(() -> bookAuthorRepository.saveAll(allNewBookAuthor)),
                CompletableFuture.runAsync(() -> bookFormatRepository.saveAll(allNewBookFormat)),
                CompletableFuture.runAsync(() -> bookAwardRepository.saveAll(allNewBookAward)),
                CompletableFuture.runAsync(() -> bookCharacterRepository.saveAll(allNewBookCharacters)),
                CompletableFuture.runAsync(() -> bookSettingRepository.saveAll(allNewBookSettings)),
                CompletableFuture.runAsync(() -> bookPublisherRepository.saveAll(allNewBookPublishers)),
                CompletableFuture.runAsync(() -> seriesCharacterRepository.saveAll(allNewSeriesCharacter)),
                CompletableFuture.runAsync(() -> ratingByStarsRepository.saveAll(ratingByStars))
        ).join();

        List<FileInfo> originalImages = getFileInfos();

//        List<FileInfo> smallImages = fileRepository.getAllSmallImages();
//        smallImages.forEach(smallImage -> {
//                    if (imageCtr.incrementAndGet() <= 5000) {
//                        smallImage.setFileDownloadStatus(FileDownloadStatus.DOWNLOADING);
//                        imageLoaderService.downloadImage(smallImage);
//                    }
//                }
//        );

        fileRepository.saveAll(originalImages);
        return allBooks.size();
    }

    private List<FileInfo> getFileInfos() {
        List<FileInfo> originalImages = fileRepository.getAllOriginalImages();
        originalImages.forEach(original -> {
                    if (imageCtr.incrementAndGet() <= 5000) {
                        original.setFileDownloadStatus(FileDownloadStatus.DOWNLOADING);
//                    byte[] imageBytes = null;
                        imageLoaderService.downloadImage(original, null);
                        original.setFileDownloadStatus(FileDownloadStatus.COMPLETED);
//                    try {
//                        // imageResizeService.saveResizedImg("path", null);
//                    } catch (IOException e) {
//
//                    }
                    }
                }
        );
        return originalImages;
    }

    public int uploadAndSaveFile(MultipartFile file) throws UnableParseFileException {
        List<BookCsvDto> csvDTos = csvReaderService.uploadBooks(file);
        if (csvDTos.isEmpty()) return 0;
        // TODO if size length equal 0 then return
        return saveBook(csvDTos);
    }
}

