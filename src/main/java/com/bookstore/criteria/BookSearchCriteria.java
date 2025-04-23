package com.bookstore.criteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookSearchCriteria extends SearchCriteria{
    private String genre;
    private String publisher;
    private String author;
    private String award;
}
