package com.bookstore.books.mapper;

import com.bookstore.books.entity.Series;
import com.bookstore.books.dto.csvDto.BookCsvDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BookDtoToSeriesMapper {
    public Series mapBookToSeries(String bookCsvDto,
                                  Map<String, Series> allSeriesExist,
                                  List<Series> allNewSeries) {

        if (!bookCsvDto.isBlank() && !bookCsvDto.equals("\"\"")) {
            String cleanSeries = bookCsvDto.split("#")[0].trim().toLowerCase();
            Series newSeries1 = allSeriesExist.computeIfAbsent(cleanSeries, Series::new);

            allNewSeries.add(newSeries1);
            return newSeries1;
        }

        String cleanTitle = bookCsvDto.toLowerCase().trim();
        Series newSeries = allSeriesExist.computeIfAbsent(cleanTitle, Series::new);
            allNewSeries.add(newSeries);
        return newSeries;
    }
}
