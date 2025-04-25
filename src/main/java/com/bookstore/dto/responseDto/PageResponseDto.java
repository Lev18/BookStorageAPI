package com.bookstore.dto.responseDto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
public class PageResponseDto<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElement;

    public static <T> PageResponseDto<T> from(Page<T> page) {
        PageResponseDto pageResponseDto = new PageResponseDto<>();
        pageResponseDto.setContent(page.getContent());
        pageResponseDto.setPageSize(page.getSize());
        pageResponseDto.setPageNumber(page.getNumber());
        pageResponseDto.setTotalPages(page.getTotalPages());
        pageResponseDto.setTotalElement(page.getTotalElements());

        return pageResponseDto;
    }
}
