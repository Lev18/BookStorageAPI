package com.bookstore.books.criteria;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;

@Getter
@Setter
public class SearchCriteria {
    private static final int DEFAULT_PAGE_SIZE = 20;

    private int page;
    private int size;

    public PageRequest buildPageRequest() {
        int pageNumber = Math.max(page, 0);
        int pageSize = size <= 0 ? DEFAULT_PAGE_SIZE : size;

        return PageRequest.of(pageNumber, pageSize);
    }
}
