package com.bookstore.books.mapper;

import com.bookstore.books.entity.Book;
import com.bookstore.books.entity.FileInfo;
import com.bookstore.books.enums.FileDownloadStatus;
import com.bookstore.books.utils.imageLoader.ImageLoaderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.swing.filechooser.FileSystemView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class BookDtoToImageMapper {
    @Value("${image.file.path}")
    private String imagePath;

    @Transactional
    public void mapImageFileFromBookDto(Book book,
                                        String coverImgUrl,
                                        List<FileInfo> allNewImages,
                                        ImageLoaderService imageLoaderService,
                                        AtomicInteger imageCtr) {
        if (imageCtr.incrementAndGet() <= 5000) {
            if (isValidUrl(coverImgUrl)) {
                String[] fileFormat = coverImgUrl.split("\\.");
                FileInfo image = new FileInfo();
                image.setFileUrl(coverImgUrl);
                image.setFileFormat(fileFormat[fileFormat.length - 1]);
                image.setFileDownloadStatus(FileDownloadStatus.PENDING);
                image.setFilePath(getFileUrl(coverImgUrl, "original"));
                image.setBook(book);
                allNewImages.add(image);
                try {
                    image.setFileDownloadStatus(FileDownloadStatus.DOWNLOADING);
                    imageLoaderService.downloadImage(image);
                    image.setFileDownloadStatus(FileDownloadStatus.COMPLETED);
                } catch (Exception e) {
                    image.setFileDownloadStatus(FileDownloadStatus.FAILED);
                }
            }
        }
    }

    public String getFileUrl(String url, String directory) {
        String[] urlParts = url.split("/");
        FileSystemView view = FileSystemView.getFileSystemView();
        String ImageFilePath = view.getHomeDirectory().getPath() + imagePath + directory;
        return ImageFilePath + "/" +
                urlParts[urlParts.length - 2] + "/" +
                urlParts[urlParts.length - 1];
    }

    private boolean isValidUrl(String url) {
        try {
            new java.net.URL(url).toURI();
            return true;
        }catch (Exception e) {
            return false;
        }
    }
}
