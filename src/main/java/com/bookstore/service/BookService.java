package com.bookstore.service;

import com.bookstore.criteria.BookSearchCriteria;
import com.bookstore.dto.requestDto.AwardDto;
import com.bookstore.dto.responseDto.*;
import com.bookstore.entity.*;
import com.bookstore.exception.ImageNotFound;
import com.bookstore.repository.*;
import com.bookstore.utils.imageLoader.ImageLoaderService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookService {

    private final ImageLoaderService imageLoader;
    private final EntityManager entityManager;

    private final BookRepository bookRepository;
    private final AwardsRepository awardsRepository;
    private final BookAwardRepository bookAwardRepository;
    private final GenreRepository genreRepository;
    private final RatingByStarsRepository ratingByStarsRepository;

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

    public void uploadImg() throws InterruptedException {
        Semaphore semaphore = new Semaphore(20);
        List<String> books = bookRepository.getAllUrls();
        for (String bookUrl : books) {
            semaphore.acquire();
            CompletableFuture.runAsync(() -> {
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

        // move to application properties
        // how get original or resized
        // with parameter enum
        // truck uploaded images keep also download status in db keep images
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

  public int insetNewBook(BookInfoDTO bookInfoDTO) {
//        List<Book> allBooks                         = Collections.synchronizedList(new ArrayList<>());
//        List<BookGenre> allNewBookGenres            = Collections.synchronizedList(new ArrayList<>());
//        List<BookAuthor> allNewBookAuthor           = Collections.synchronizedList(new ArrayList<>());
//        List<BookFormat> allNewBookFormat           = Collections.synchronizedList(new ArrayList<>());
//        List<BookAward> allNewBookAward             = Collections.synchronizedList(new ArrayList<>());
//        List<BookPublisher> allNewBookPublishers    = Collections.synchronizedList(new ArrayList<>());
//        List<SeriesCharacter> allNewSeriesCharacter = Collections.synchronizedList(new ArrayList<>());
//        List<BookSetting> allNewBookSettings        = Collections.synchronizedList(new ArrayList<>());
//
//        List<Publisher> allNewPublishers         = Collections.synchronizedList(new ArrayList<>());
//        List<BookCharacter> allNewBookCharacters = Collections.synchronizedList(new ArrayList<>());
//        List<Genre> allNewGenres                 = Collections.synchronizedList(new ArrayList<>());
//        List<Format> allNewFormats               = Collections.synchronizedList(new ArrayList<>());
//        List<Author> allNewAuthors               = Collections.synchronizedList(new ArrayList<>());
//        List<Award> allNewAwards                 = Collections.synchronizedList(new ArrayList<>());
//        List<Setting> allNewSettings             = Collections.synchronizedList(new ArrayList<>());
//        List<Characters> allNewCharacters        = Collections.synchronizedList(new ArrayList<>());
//        List<Series> allNewSeries                = Collections.synchronizedList(new ArrayList<>());
//        Set<String> uniqueIsbn                   = Collections.synchronizedSet(new HashSet<>());
//
//        Set<RatingByStars> ratingByStars = Collections.synchronizedSet(new HashSet<>());
//
//
//        Map<String, Author> allExistingAuthors = authorRepository.findAll().stream()
//                .collect(Collectors.toConcurrentMap(author -> author.getAuthorName()
//                                .toLowerCase().trim(),
//                        author -> author));
//
//
//        Map<String, Author> allExistingAuthors = authorRepository.findAll().stream()
//                .collect(Collectors.toConcurrentMap(author -> author.getAuthorName()
//                                .toLowerCase().trim(),
//                        author -> author));
//
//
//        Map<String, Characters> allExistingCharacters = charactersRepository.findAll().stream()
//                .collect(Collectors.toConcurrentMap(characters -> characters.getCharacterName()
//                                .toLowerCase().trim(),
//                        characters -> characters));
//
//
//        Map<String, Award> allExistingAwards = awardsRepository.findAll().stream()
//                .collect(Collectors.toConcurrentMap(award -> award.getName()
//                                .toLowerCase().trim(),
//                        award -> award));
//
//
//        Map<String, Setting> allExistingSettings = settingRepository.findAll().stream()
//                .collect(Collectors.toConcurrentMap(setting -> setting.getSetting()
//                                .toLowerCase().trim(),
//                        setting -> setting));
//
//
//        Map<String, Genre> allExistingGenres = genreRepository.findAll().stream()
//                .collect(Collectors.toConcurrentMap(genre -> genre.getGenreTitle()
//                                .toLowerCase().trim(),
//                        genre -> genre));
//
//
//        Map<String, Format> allFormatExists = formatRepository.findAll().stream()
//                .collect(Collectors.toConcurrentMap(format -> format.getFormat()
//                                .toLowerCase().trim(),
//                        format -> format));
//
//        Map<String, Publisher> allPublisherExists = publisherRepository.findAll().stream()
//                .collect(Collectors.toConcurrentMap(publisher -> publisher.getPublisherName()
//                                .toLowerCase().trim(),
//                        publisher -> publisher));
//
//        Map<String, Series> allSeriesExists = seriesRepository.findAll().stream()
//                .collect(Collectors.toConcurrentMap(series -> series.getSeries()
//                                .toLowerCase().trim(),
//                        series -> series));
//
//        Book book = bookInfoDtoToBookMapper.mapBootDtoIntoBook(bookInfoDTO);
//
//        Series series = bookDtoToSeriesMapper.mapBookToSeries(bookInfoDTO,
//                allSeriesExists, allNewSeries);
//        if (series != null) book.setSeries(series);
//
//        allBooks.add(book);
//        ratingByStars.addAll(book.getStars());
//
//        allNewBookAuthor.addAll(bookToAuthorMapper.bookToAuthorMapper(bookInfoDTO,
//                        allExistingAuthors, allNewAuthors).stream()
//                .map(author -> new BookAuthor(book, author)).toList());
//
//        allNewBookGenres.addAll(bookToGenreMapper.findOrCreateGenre(bookInfoDTO,
//                        allExistingGenres, allNewGenres).stream()
//                .map(genre -> new BookGenre(book, genre)).toList());
//
//        allNewBookFormat.addAll(bookDtoToFormatMapper.mapBookToFormat(bookInfoDTO,
//                        allFormatExists, allNewFormats).stream()
//                .map(format -> new BookFormat(book, format)).toList());
//
//        bookDtoToAwardMapper.bookToAwardMapper(bookInfoDTO,
//                allExistingAwards, allNewAwards).forEach(
//                (awardType, award) -> allNewBookAward
//                        .add(new BookAward(book, award, awardType))
//        );
//
//        for (Characters c : characterMapper.mapBookDtoToCharacter(bookInfoDTO,
//                allExistingCharacters, allNewCharacters)) {
//            allNewBookCharacters.add(new BookCharacter(book, c));
//            if (series != null) {
//                allNewSeriesCharacter.add(new SeriesCharacter(series, c));
//            }
//        }
//
//        allNewBookSettings.addAll(bookDtoToSettingMapper.mapBookDtoToSetting(bookInfoDTO,
//                        allExistingSettings, allNewSettings).stream()
//                .map(setting -> new BookSetting(book, setting)).toList());
//
//        allNewBookPublishers.addAll(bookDtoToPublisherMapper.bookToPublisherMapper(bookInfoDTO,
//                        allPublisherExists, allNewPublishers).stream()
//                .map(publisher -> new BookPublisher(book, publisher)).toList());
//
//        if (book != null) bookRepository.save(book);
//
       return 0;
   }

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
        for (BookFlatRecord bookFlatRecord : books){
            BookResponseDto bookResponseDto = bookMap.computeIfAbsent(bookFlatRecord.id(),
                    id -> new BookResponseDto(bookFlatRecord.id(),
                            bookFlatRecord.title(),
                            bookFlatRecord.series(),
                            bookFlatRecord.publishDate(),
                            bookFlatRecord.rating()
                            ) );
            bookResponseDto.addAuthor(bookFlatRecord.author());
            bookResponseDto.addAward(bookFlatRecord.award());
            bookResponseDto.addCharacter(bookFlatRecord.character());
            bookResponseDto.addGenre(bookFlatRecord.genre());
            bookResponseDto.addPublisher(bookFlatRecord.publisher());
        }
        return bookMap;
    }
}
//Long id,
//String title,
//String series,
//String publishDate,
//Integer rating

