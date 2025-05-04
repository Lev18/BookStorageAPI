package com.bookstore.books.mapper;

import com.bookstore.books.entity.Book;
import com.bookstore.books.entity.FileInfo;
import com.bookstore.books.enums.FileDownloadStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.swing.filechooser.FileSystemView;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookDtoToImageMapper {
    @Value("{image.file.path}")
    private String imagePath;

    @Transactional
    public void mapImageFileFromBookDto(Book book,
                                                  String coverImgUrl,
                                                  List<FileInfo> allNewImages) {
        List<FileInfo> imageFile = new ArrayList<>();
        if (isValidUrl(coverImgUrl)) {

            String[] fileFormat = coverImgUrl.split("\\.");
            for (int i = 0; i < 3; ++i) {
                FileInfo image = new FileInfo();
                image.setFileUrl(coverImgUrl);
                image.setFileFormat(fileFormat[fileFormat.length - 1]);
                image.setFileDownloadStatus(FileDownloadStatus.PENDING);
                switch (i) {
                    case 0:
                        image.setFilePath(getFileUrl(coverImgUrl, "original"));
                        break;
                    case 1:
                        image.setFilePath(getFileUrl(coverImgUrl, "medium"));
                        break;
                    case 2:
                        image.setFilePath(getFileUrl(coverImgUrl, "small"));
                        break;
                }
                image.setBook(book);
                imageFile.add(image);
                allNewImages.add(image);
            }
        }
    }

    public String getFileUrl(String url, String directory) {
        String[] urlParts = url.split("/");
        FileSystemView view = FileSystemView.getFileSystemView();
        // move to application properties


        String ImageFilePath = view.getHomeDirectory().getPath() + imagePath + directory;
        String outputFile = ImageFilePath + "/" +
                urlParts[urlParts.length - 2] + "/" +
                urlParts[urlParts.length - 1];
        return outputFile;
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
