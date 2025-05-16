package com.bookstore.books.service;

import com.bookstore.books.criteria.SearchCriteria;
import com.bookstore.books.dto.requestDto.GenreRequestDto;
import com.bookstore.books.dto.responseDto.*;
import com.bookstore.books.entity.Book;
import com.bookstore.books.entity.BookGenre;
import com.bookstore.books.entity.Genre;
import com.bookstore.books.repository.BookGenreRepository;
import com.bookstore.books.repository.GenreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class GenreService {
    private final int MAX_LENGTH = 300;
    private final GenreRepository genreRepository;
    private final EntityManager entityManager;
    private final BookGenreRepository bookGenreRepository;

    public void deleteGenre(Long id) {
       if (!genreRepository.existsById(id)) {
           throw new EntityNotFoundException("Entity not found");
       }

       bookGenreRepository.deleteAllByGenreId(id);
       genreRepository.deleteById(id);
    }

    public Optional<PageResponseDto<GenreWithBookDto>> getGetGenresBook(String genre,
                                                        SearchCriteria searchCriteria,
                                                        Pageable pageable) {
       if (genreRepository.findByName(genre) == null) {
           throw new EntityNotFoundException("Genre not found");
       }
       return  genreRepository.findWithBooks(
               genre,
               searchCriteria,
               pageable ).map(this::toBookDto);

    }

    private PageResponseDto<GenreWithBookDto> toBookDto(@NotNull Page<Object[]> genres) {
        return PageResponseDto.from(new PageImpl<>(
                genres.map(this::mapToBookMin).toList(),
                genres.getPageable(),
                genres.getTotalElements()));
    }

    private GenreWithBookDto mapToBookMin(Object[] genre) {
        Long genreId = (Long) genre[0];
        String genreName = (String) genre[1];
        Long bookId = (Long) genre[2];
        String bookTitle = (String) genre[3];
        String bookDescription = ((String)genre[4]).length() >= MAX_LENGTH ?
                ((String)genre[4]).substring(0, MAX_LENGTH) :
                ((String)genre[4]);
        String awgRating = (String)genre[5];
        String author = (String)genre[6];
        String series = (String) genre[7];
        String publishDate = (String)genre[8];
        String firstPublishDate = (String)genre[9];

        GenreWithBookDto newGenre = new GenreWithBookDto();
        newGenre.setId(genreId);
        newGenre.setName(genreName);
        newGenre.getBooks().add( new BookMiniDto(
                bookId,
                bookTitle,
                bookDescription.length() >= MAX_LENGTH ?
                        bookDescription.substring(0, MAX_LENGTH):
                        bookDescription,
                awgRating,
                publishDate.equals("\"\"") ? firstPublishDate : publishDate,
                Arrays.stream(author.split(","))
                        .collect(Collectors.toSet()),
                series != null ?
                        Arrays.stream(series.split(","))
                                .collect(Collectors.toSet()) :
                        null
        ));

        return newGenre;
    }

    public List<GenreWithBookDto> getTopFiveGenresFiveBooks() {
        List<Object[]> result = genreRepository.findTopFiveGenreFiveBooks().orElseThrow();
        return mapToGenreDto(result);
    }

    private List<GenreWithBookDto> mapToGenreDto(List<Object[]> objects) {
        Map<Long, List<GenreWithBookDto>> genreMap = new LinkedHashMap<>();
        return objects.stream().map(this::mapToBookMin).toList();
    }

    public Optional<PageResponseDto<GenreInfoDTO>> getGenreList(SearchCriteria searchCriteria,
                                                     Pageable pageable) {
        return genreRepository
                .findAllWithBooksCount(searchCriteria, pageable)
                .map(this::toGnDtoPage);
    }


    private PageResponseDto<GenreInfoDTO> toGnDtoPage(@NotNull Page<Object[]> objects) {
        return PageResponseDto.from(new PageImpl<>(objects.map(this::toGenreDto).toList(),
                objects.getPageable(), objects.getTotalElements()));
    }

    private GenreInfoDTO toGenreDto(@NotNull Object[] genre) {
        return  new GenreInfoDTO(
                ((Genre)genre[0]).getId(),
                ((Genre)genre[0]).getName(),
                (Long)genre[1]
        );
    }

    public GenreResponseDto createGenre(GenreRequestDto dto) {
        if (genreRepository.findByName(dto.getName()) != null) {
            throw  new IllegalArgumentException("Genre already exist");
        }
        Genre genre = new Genre(dto.getName());
        genreRepository.save(genre);

        return mapToResponse(genre);
    }

    private GenreResponseDto mapToResponse(Genre genre) {
        GenreResponseDto genreResponseDto = new GenreResponseDto();
        genreResponseDto.setId(genre.getId());
        genreResponseDto.setName(genre.getName());

        return genreResponseDto;
    }

    public GenreResponseDto updateGenre(Long id, GenreRequestDto dto) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(()-> {
                    throw new IllegalArgumentException("Genre not found");
                });
        genre.setName(dto.getName());
        genreRepository.save(genre);

        return mapToResponse(genre);
    }
}
