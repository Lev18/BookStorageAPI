package com.bookstore.criteria;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchCriteria {
    private static final int DEFAULT_PAGE_SIZE = 20;

    private int page;
    private int size;
}
