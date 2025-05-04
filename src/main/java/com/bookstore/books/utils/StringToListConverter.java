package com.bookstore.books.utils;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringToListConverter extends AbstractBeanField<String, List<String>> {
    @Override
    protected List<String> convert(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }

        Pattern outerBracketsQuotes = Pattern.compile("^\\[?\"?|\"?\\]?$");
        Pattern allQuotes = Pattern.compile("\"");
        Pattern commaSplit = Pattern.compile("\\s*,\\s*");

        String cleaned = outerBracketsQuotes.matcher(value).replaceAll("");
        cleaned = allQuotes.matcher(cleaned).replaceAll("");
        return commaSplit.splitAsStream(cleaned)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());

    }
}
