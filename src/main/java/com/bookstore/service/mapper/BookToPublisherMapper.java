package com.bookstore.service.mapper;

import com.bookstore.entity.Publisher;
import com.bookstore.repository.PublisherRepository;
import com.bookstore.service.dto.BookCsvDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class BookToPublisherMapper {
    // TODO refactor Publisher table into many to many
    @Transactional
    public Publisher bookToPublisherMapper(BookCsvDto bookDto,
                                           Set<Publisher> publishersInDb,
                                           List<Publisher> newPublishers) {
        Publisher newPublisher = publishersInDb.stream()
                .filter(publisher -> publisher.getPublisherName().equals(bookDto.getPublisher()))
                .findFirst()
                .orElseGet(() ->{
                    Publisher publisher = new Publisher(bookDto.getPublisher());
                    return publisher;
        });
        publishersInDb.add(newPublisher);
        newPublishers.add(newPublisher);
        return  newPublisher;
    }
}
