package com.bookstore.Service.FileReader;

import com.bookstore.Repository.FileRepository;
import com.bookstore.Service.dto.BookCsvDto;
import com.bookstore.utils.HashUtils;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class CsvReaderService {
    @Autowired
    FileRepository fileRepository;

    public  List<BookCsvDto>  uploadBooks(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        String hash = HashUtils.computeSHA256(file);
        if ( fileRepository.existsByFileHash(hash)) {
            System.out.println("File already processed");
            return List.of();
        }
        FileHash fileHash = new FileHash(hash);
        List<BookCsvDto> books = parseCsv(file);
         fileRepository.save(fileHash);
        return books;
    }

    private List<BookCsvDto> parseCsv(MultipartFile file) throws IOException {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            HeaderColumnNameMappingStrategy<BookCsvDto> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(BookCsvDto.class);
            List<String> cleanedLine = reader.lines()
                    .map(line-> line
                            .replaceAll("\\\\\"", "\"")  // Replace \" with "
                            .replaceAll("\"\"", "\\\\\"\\\\\"")
                    )
                    .collect(Collectors.toUnmodifiableList());


            CsvToBean<BookCsvDto> csvDtos = new CsvToBeanBuilder<BookCsvDto>( new StringReader(String.join("\n",cleanedLine)))
                    .withMappingStrategy(strategy)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .withIgnoreEmptyLine(true)
                    .build();

            List<BookCsvDto> bookDtos =  csvDtos.parse()
                    .stream()
                    .map(csvLine-> {
                                BookCsvDto bookCsvDto = new BookCsvDto();
                                try {
                                    BeanUtils .copyProperties(csvLine, bookCsvDto);
                                } catch (IllegalAccessError e) {
                                    e.printStackTrace();
                                }
                                return bookCsvDto;
                            }
                    )
                    .collect(Collectors.toList());
            return bookDtos;
        }
      //  "1885.Pride_and_Prejudice,Pride and Prejudice,,\"Jane Austen, Anna Quindlen (Introduction)\" +,4.26,\"Alternate cover edition of ISBN 9780679783268Since its immediate success in 1813, Pride and Prejudice has remained one of the most popular novels in the English language. Jane Austen called this brilliant work \" \"her own darling child"" and its vivacious heroine, Elizabeth Bennet, \"\"as delightful a creature as ever appeared in print.\"\" The romantic clash between the opinionated Elizabeth and her proud beau, Mr. Darcy, is a splendid performance of civilized sparring. And Jane Austen's radiant wit sparkles as her characters dance a delicate quadrille of flirtation and intrigue, making this book the most superb comedy of manners of Regency England.\",English,9999999999999,\"['Classics', 'Fiction', 'Romance', 'Historical Fiction', 'Literature', 'Historical', 'Novels', 'Historical Romance', 'Classic Literature', 'Adult']\",\"['Mr. Bennet', 'Mrs. Bennet', 'Jane Bennet', 'Elizabeth Bennet', 'Mary Bennet', 'Kitty Bennet', 'Lydia Bennet', 'Louisa Hurst', 'Caroline Bingley', 'Fitzwilliam Darcy', 'Georgiana Darcy', 'Lady Catherine de Bourgh', 'Anne de Bourgh', 'Colonel Fitzwilliam', 'Mr. Gardiner', 'Mrs. Gardiner', 'Sir William Lucas', 'Lady Lucas', 'Charlotte Lucas', 'Maria Lucas', 'Mr. Darcy', 'Charles Bingley', 'George Wickham', 'Mr. William Collins']\",Paperback,\"Modern Library Classics, USA / CAN\",279,Modern Library,10/10/00,01/28/13,[],2998241,\"['1617567', '816659', '373311', '113934', '76770']",94,"['United Kingdom', 'Derbyshire, England (United Kingdom)', 'England', 'Hertfordshire, England (United Kingdom)']\",https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1320399351l/1885.jpg,1983116,20452";
    }
}
