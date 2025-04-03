package com.bookstore.utils;

import com.opencsv.bean.AbstractBeanField;
import com.opencsv.exceptions.CsvConstraintViolationException;
import com.opencsv.exceptions.CsvDataTypeMismatchException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringToListConverter extends AbstractBeanField<String, List<String>> {
    @Override
    protected List<String> convert(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        //return Arrays.asList(value.split("\\s*, \\s*"));
        return Arrays.stream(value
                        .replaceAll("^\\[?\"?|\"?\\]?$", "") // Remove [ ] and quotes
                        .replaceAll("\"", "")
                        .split("\\s*,\\s*"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
