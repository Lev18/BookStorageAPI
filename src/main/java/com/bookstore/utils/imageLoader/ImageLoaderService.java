package com.bookstore.utils.imageLoader;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
public class ImageLoaderService {

    private final ImageResizeService imageResizeService;

    @Async
    public void downloadImage(String url) {
        //TODO: find validation method for url
        if (!url.isBlank() && !url.equals("\"\"")) {
            try (InputStream in = new URL(url)
                                 .openStream()) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer, 0, len);
                }
                byte[] imageBytes = byteArrayOutputStream.toByteArray();

                // save original image
                File originalFile = getFile(url, "original");
                Files.write(originalFile.toPath(),
                        imageBytes);
                System.out.println("Current dir:" +originalFile.getParentFile());

                // save resized image
                imageResizeService.saveResizedImg(url, imageBytes);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public File getFile(String url, String directory) {
        String[] urlParts = url.split("/");
        FileSystemView view = FileSystemView.getFileSystemView();
        // move to application properties
        String ImageFilePath = view.getHomeDirectory().getPath() + "/Workspace/ImagesBook/" + directory;
        File outputFile = new File(ImageFilePath + "/" +
                urlParts[urlParts.length - 2] + "/" +
                urlParts[urlParts.length - 1]);
        outputFile.getParentFile().mkdirs();
        return outputFile;
    }
}
