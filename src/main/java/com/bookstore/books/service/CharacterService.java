package com.bookstore.books.service;

import com.bookstore.books.criteria.SearchCriteria;
import com.bookstore.books.dto.requestDto.CharacterRequestDto;
import com.bookstore.books.dto.responseDto.*;
import com.bookstore.books.entity.Characters;
import com.bookstore.books.entity.Genre;
import com.bookstore.books.repository.BookCharactersRepository;
import com.bookstore.books.repository.CharactersRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CharacterService {
    private final CharactersRepository charactersRepository;
    private final BookCharactersRepository bookCharactersRepository;
    private final int MAX_LENGTH = 300;


    public CharacterResponseDto insertNewCharacter(@Valid CharacterRequestDto dto) {
        if (charactersRepository.existsByName(dto.getName())) {
            throw new IllegalArgumentException("Character already exist");
        }
        Characters character = new Characters();
        character.setName(dto.getName());

        charactersRepository.save(character);

        return mapToResponseDto(character);
    }

    private CharacterResponseDto mapToResponseDto(Characters character) {
        CharacterResponseDto responseDto = new CharacterResponseDto();
        responseDto.setId(character.getId());
        responseDto.setName(character.getName());

        return responseDto;
    }

    public Optional<PageResponseDto<CharacterResponseDto>> getAllCharacters(SearchCriteria searchCriteria,
                                                                 Pageable pageable) {
        return charactersRepository.findAllCharacters(searchCriteria, pageable)
                .map(this::mapToDto);
    }

    private PageResponseDto<CharacterResponseDto> mapToDto(@NotNull
                                                Page<Characters> characterResponseDtos) {
        return PageResponseDto.from(new PageImpl<>(characterResponseDtos.map(this::mapToResponseDto).toList(),
                characterResponseDtos.getPageable(),
                characterResponseDtos.getTotalElements()));
    }


    public Optional<CharacterWithBookDto> getCharacterWithBooks(Long id) {
        if (!charactersRepository.existsById(id)) {
            throw new EntityNotFoundException("Character not found");
        }

        return charactersRepository.findCharacterWithBookById(id)
                .map(this::mapToBookMin);
    }

    private CharacterWithBookDto mapToBookMin(List<Object[]> characterObj) {
        CharacterWithBookDto dto = new CharacterWithBookDto();
        for (Object[] character : characterObj) {
            Long characterId = (Long) character[0];
            String characterName = (String) character[1];
            Long bookId = (Long) character[2];
            String bookTitle = (String) character[3];
            String bookDescription = (String) character[4];
            String avgRating = (String) character[5];
            String author = (String) character[6];
            String series = (String) character[7];
            String publishDate = (String) character[8];
            String firstPublishDate = (String) character[9];

            String finalDescription = bookDescription != null && bookDescription.length() > MAX_LENGTH
                    ? bookDescription.substring(0, MAX_LENGTH)
                    : bookDescription;

            Set<String> authors = author != null ? Arrays.stream(author.split(",")).collect(Collectors.toSet()) : Set.of();
            Set<String> seriesSet = series != null ? Arrays.stream(series.split(",")).collect(Collectors.toSet()) : null;

            BookMiniDto bookMiniDto = new BookMiniDto(
                    bookId,
                    bookTitle,
                    finalDescription,
                    avgRating,
                    (publishDate == null || publishDate.equals("\"\"")) ? firstPublishDate : publishDate,
                    authors,
                    seriesSet
            );

            dto.setId(characterId);
            dto.setName(characterName);
            dto.setBooks(new ArrayList<>(List.of(bookMiniDto)));
        }
        return dto;
    }

    public CharacterResponseDto updateCharacter(Long id, 
                                            CharacterRequestDto dto) {
        Characters character = charactersRepository.findById(id)
                .orElseThrow(()-> {
                    throw new IllegalArgumentException("Character not found");
                });
        character.setName(dto.getName());
        charactersRepository.save(character);

        return mapToResponse(character);
    }

    private CharacterResponseDto mapToResponse(Characters character) {
       CharacterResponseDto characterResponseDto = new CharacterResponseDto();
       characterResponseDto.setId(character.getId());
       characterResponseDto.setName(character.getName());
       return characterResponseDto;
    }

    public void deleteCharacter(Long id) {
        if (!charactersRepository.existsById(id)) {
            throw new EntityNotFoundException("Entity not found");
        }

        bookCharactersRepository .deleteAllByGenreId(id);
        charactersRepository.deleteById(id);
    }
}
