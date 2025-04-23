package com.bookstore.mapper;

import com.bookstore.entity.Publisher;
import com.bookstore.dto.csvDto.BookCsvDto;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BookToPublisherMapper {
    // TODO refactor Publisher table into many to many
    public List<Publisher> bookToPublisherMapper(BookCsvDto bookDto,
                                           Map<String, Publisher> allPublishersExist,
                                           List<Publisher> allNewPublishers) {
        List<String> publishers = Arrays.stream(bookDto.getPublisher().split("/"))
                                           .toList();
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
