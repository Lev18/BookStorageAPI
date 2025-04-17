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
        List<Publisher> publishers = Arrays.stream(bookDto.getPublisher().split("/"))
                                           .map(Publisher::new)
                                           .toList();
        List<Publisher> allPublishers = new ArrayList<>();

        for (Publisher publisher : publishers) {
            Publisher existPublisher = allPublishersExist.get(publisher.getPublisherName().toLowerCase());
            if (existPublisher != null) {
                allPublishers.add(existPublisher);
            } else {
                allPublishers.add(publisher);
                allNewPublishers.add(publisher);
                allPublishersExist.put(publisher.getPublisherName(), publisher);
            }
        }
        return  allPublishers;
    }
}
