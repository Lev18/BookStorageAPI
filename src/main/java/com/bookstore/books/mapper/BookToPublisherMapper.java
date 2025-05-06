package com.bookstore.books.mapper;

import com.bookstore.books.entity.Publisher;
import com.bookstore.books.dto.csvDto.BookCsvDto;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;

@Component
public class BookToPublisherMapper {
    public <T> List<Publisher> mapBookToPublisher(T bookDto,
                                                  Function<T, Collection<String>> publishExtractor,
                                                  Map<String, Publisher> allPublishersExist,
                                                  List<Publisher> allNewPublishers) {

        Collection<String> publisher = publishExtractor.apply(bookDto);
        return mapToPublisher(publisher,
                allPublishersExist,
                allNewPublishers);
    }
    public List<Publisher> mapToPublisher(Collection<String> publishers,
                                           Map<String, Publisher> allPublishersExist,
                                           List<Publisher> allNewPublishers) {

        List<Publisher> allPublishers = new ArrayList<>();
        for (String publisher : publishers) {
            String cleanString = publisher.trim().toLowerCase();
            Publisher existPublisher = allPublishersExist.computeIfAbsent(cleanString, key-> {
                Publisher newPublisher = new Publisher(cleanString);
                allNewPublishers.add(newPublisher);
                return newPublisher;
            });

            allPublishers.add(existPublisher);
        }
        return  allPublishers;
    }
}
