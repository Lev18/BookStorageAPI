package com.bookstore.books.utils.imageLoader;


import com.bookstore.books.entity.FileInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

@Service
public class ImageResizeService {
    @Value("${image.file.path}")
    private String imagePath;
    private final String SMALL_DIRECTORY = "small";
    private final String MEDIUM_DIRECTORY = "medium";
    public void saveResizedImg(FileInfo fileInfo,
                               BufferedImage image,
                               int targetWidth,
                               int targetHeight,
                               String directory) {

        try {
            BufferedImage resizedImage = resizeImg(image,
                    targetWidth,
                    targetHeight);

            File resizedFile = getFile(fileInfo.getFileUrl(), directory);
            if (!ImageIO.write(resizedImage, "jpg", resizedFile)) {
                System.out.println("Filed to save resized image");
            }
        } catch (Exception ex) {
            System.out.println("Error resizing image for URL: " + fileInfo.getFileUrl());
            System.out.println(ex.getMessage());
        }
    }

    @Async
    public BufferedImage resizeImg(BufferedImage originalImage,
                                   int targetWidth,
                                   int targetHeight) {
        Image resultImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImg = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = outputImg.createGraphics();
        g2d.drawImage(resultImage, 0,0, null);
        g2d.dispose();
        return outputImg;

    }

    public File getFile(String url, String directory) {
        String[] urlParts = url.split("/");
        FileSystemView view = FileSystemView.getFileSystemView();
        String ImageFilePath = view.getHomeDirectory().getPath() + imagePath + directory;
        File outputFile = new File(ImageFilePath + "/" +
                urlParts[urlParts.length - 2] + "/" +
                urlParts[urlParts.length - 1]);
        outputFile.getParentFile().mkdirs();
        return outputFile;
    }

    public void saveSmallImage(FileInfo fileInfo) {
        try {
            File file = new File(fileInfo.getFilePath());
            BufferedImage image = ImageIO.read(file);
            int width = image.getWidth() / 4;
            int height = image.getHeight() / 4;
            saveResizedImg(fileInfo,
                    image,
                    height,
                    width,
                    SMALL_DIRECTORY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void saveMediumImage(FileInfo fileInfo) {

        try {
            File file = new File(fileInfo.getFilePath());
            BufferedImage image = ImageIO.read(file);
            int width = image.getWidth() / 2;
            int height = image.getHeight() / 2;
            saveResizedImg(fileInfo,
                    image,
                    height,
                    width,
                    MEDIUM_DIRECTORY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
