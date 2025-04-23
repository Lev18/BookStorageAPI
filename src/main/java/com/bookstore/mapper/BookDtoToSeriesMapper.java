package com.bookstore.mapper;

import com.bookstore.entity.Series;
import com.bookstore.dto.csvDto.BookCsvDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class BookDtoToSeriesMapper {
    public Series mapBookToSeries(BookCsvDto bookCsvDto,
                                  Map<String, Series> allSeriesExist,
                                  List<Series> allNewSeries) {

        if (!bookCsvDto.getSeries().isBlank() && !bookCsvDto.getSeries().equals("\"\"")) {
            String cleanSeries = bookCsvDto.getSeries().split("#")[0].trim().toLowerCase();
            Series newSeries1 = allSeriesExist.computeIfAbsent(cleanSeries, Series::new);

            allNewSeries.add(newSeries1);
            return newSeries1;
        }

        String cleanTitle = bookCsvDto.getTitle().toLowerCase().trim();
        Series newSeries = allSeriesExist.computeIfAbsent(cleanTitle, Series::new);
            allNewSeries.add(newSeries);
        return newSeries;
    }
}
