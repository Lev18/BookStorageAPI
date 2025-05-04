package com.bookstore.books.criteria;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class BookSearchCriteria extends SearchCriteria{
    private String genre;
    private String publisher;
    private String author;
    private String award;
    private String character;

    @Override
    public PageRequest buildPageRequest() {
        PageRequest pageRequest = super.buildPageRequest();
        return pageRequest.withSort(Sort.by("id").descending());
    }
}
