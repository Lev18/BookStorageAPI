package com.imageLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ImageLoader {
    public static void main(String[] args) {
        uploadImage("https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1551144577l/18405._SY475_.jpg");
    }
    public static void uploadImage(String url) {
        try  (InputStream in =
                   new URL(url)
                            .openStream()){
            String[] urlParts = url.split("/");
            String currentPath = new java.io.File(".").getCanonicalPath();
            File file = new File(currentPath + "/src/main/resources/static/Images/" + urlParts[urlParts.length - 1]);
            file.getParentFile().mkdirs();
            //System.out.println("Current dir:" +  file.getParentFile().toPath().toString());
           Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException ex) {
            ex.printStackTrace();
        }
    }

}
