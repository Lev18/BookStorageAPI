package com.bookstore.books.utils.imageLoader;

import com.bookstore.books.entity.FileInfo;
import com.bookstore.books.enums.FileDownloadStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ImageLoaderService {
    @Async("taskExecutor")
    public CompletableFuture<Void> downloadImage(FileInfo fileInfo) {
        //TODO: find validation method for url
                try (InputStream in = new URL(fileInfo.getFileUrl())
                        .openStream()) {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[8192];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, len);
                    }
                    byte[] imageBytes = byteArrayOutputStream.toByteArray();

                    // save original image
                    File originalFile = new File(fileInfo.getFilePath());
                    originalFile.getParentFile().mkdirs();

                    Files.write(originalFile.toPath(),
                            imageBytes);
                    System.out.println("Current dir:" + originalFile.getParentFile());

                } catch (IOException ex) {
                    fileInfo.setFileDownloadStatus(FileDownloadStatus.FAILED);
                }
            return CompletableFuture.completedFuture(null);
    }

}
