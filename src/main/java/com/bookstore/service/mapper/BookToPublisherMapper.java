package com.bookstore.service.mapper;

import com.bookstore.entity.Publisher;
import com.bookstore.repository.PublisherRepository;
import com.bookstore.service.dto.BookCsvDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BookToPublisherMapper {
    private Map<String, Publisher> PublisherCache = new HashMap<>();
    public Publisher bookToPublisherMapper(BookCsvDto bookDto, PublisherRepository PublisherRepository) {
        String bookFormat = bookDto.getBookFormat();
        if (PublisherCache.containsKey(bookFormat)) {
            return PublisherCache.get(bookFormat);
        }

        Publisher Publisher = PublisherRepository.findByPublisherName(bookDto.getPublisher())
                .orElseGet(()->PublisherRepository.save(new Publisher(bookDto.getPublisher())));
        PublisherCache.put(bookFormat, Publisher);
        return Publisher;
    }
}
