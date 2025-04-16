package com.bookstore.service.fileReader;

import com.bookstore.repository.FileRepository;
import com.bookstore.service.csvDto.BookCsvDto;
import com.bookstore.service.exception.NoSuchFile;
import com.bookstore.utils.HashUtils;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
// name confusion CsvReaderService
public class CsvReaderService {
    private final FileRepository fileRepository;
    // rename uploadBook name
    public  List<BookCsvDto>  uploadBooks(MultipartFile file) {
            String hash = HashUtils.computeSHA256(file);
            if (fileRepository.existsByFileHash(hash)) {
                System.out.println("File already processed");
                return List.of();
            }

            FileHash fileHash = new FileHash(hash);
            List<BookCsvDto> books = parseCsv(file);
            fileRepository.save(fileHash);
            return books;
    }

//TODO:create my own exception
    private List<BookCsvDto> parseCsv(MultipartFile file) {
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
                HeaderColumnNameMappingStrategy<BookCsvDto> strategy = new HeaderColumnNameMappingStrategy<>();
                strategy.setType(BookCsvDto.class);
                Pattern backslashQuote = Pattern.compile("\\\\\"");  //  \\"
                Pattern doubleQuote = Pattern.compile("\"\"");
                List<String> cleanedLine = reader.lines()
                        .map(line -> {
                                    String result = backslashQuote.matcher(line)
                                            .replaceAll("\"");
                                    result = doubleQuote.matcher(result)
                                            .replaceAll("\\\\\"\\\\\"");
                                    return result;
                                }
                        )
                        .toList();

                CsvToBean<BookCsvDto> csvDtos = new CsvToBeanBuilder<BookCsvDto>(
                        new StringReader(String.join("\n", cleanedLine)))
                        .withMappingStrategy(strategy)
                        .withSeparator(',')
                        .withIgnoreLeadingWhiteSpace(true)
                        .withIgnoreEmptyLine(true)
                        .build();

                List<BookCsvDto> bookDtos = csvDtos.parse()
                        .stream()
                        .map(csvLine -> {
                                    BookCsvDto bookCsvDto = new BookCsvDto();
                                    try {
                                        BeanUtils.copyProperties(csvLine, bookCsvDto);
                                    } catch (IllegalAccessError e) {
                                        e.printStackTrace();
                                    }
                                    return bookCsvDto;
                                }
                        )
                        .collect(Collectors.toList());
                return bookDtos;
            }
        } catch (IOException e) {
            throw new NoSuchFile("Provided file doesn't exist " + file.getName());
        }
    }
}
