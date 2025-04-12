package com.bookstore.utils.imageLoader;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
public class ImageLoader {
    @Async
    // avoid from static
    // download and resize
    public static void uploadImage(String url) {
        if (!url.isEmpty() && !url.equals("")) {
            try (InputStream in =
                         new URL(url)
                                 .openStream()) {
                String[] urlParts = url.split("/");
                String currentPath = new java.io.File(".").getCanonicalPath();
                File file = new File(currentPath + "/src/main/resources/static/Images/" + urlParts[urlParts.length - 1]);
                file.getParentFile().mkdirs();
                //System.out.println("Current dir:" +  file.getParentFile().toPath().toString());
                Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
