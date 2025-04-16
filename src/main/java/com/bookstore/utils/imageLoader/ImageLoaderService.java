package com.bookstore.utils.imageLoader;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Service
public class ImageLoaderService {
 //    @Async
    // download and resize
    public void downloadImage(String url) {
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
                saveResizedImg(url, imageBytes);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @Async
    public void saveResizedImg(String url,
                               byte[] imageBytes) throws IOException {
        File resizedFile = getFile(url, "resized");
        BufferedImage original = ImageIO.read(new ByteArrayInputStream(imageBytes));
        BufferedImage resizedImp = resizeImg(original,
                                            original.getWidth() / 4,
                                            original.getHeight() / 4);
        if (!ImageIO.write(resizedImp, "jpg", resizedFile)) {
            System.out.println("Filed to save resized image");
        }
    }

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
    private File getFile(String url, String directory) {
        String[] urlParts = url.split("/");
        FileSystemView view = FileSystemView.getFileSystemView();
        String ImageFilePath = view.getHomeDirectory().getPath() + "/Workspace/ImagesBook/" + directory;
        File outputFile = new File(ImageFilePath + "/" +
                urlParts[urlParts.length - 2] + "/" +
                urlParts[urlParts.length - 1]);
        outputFile.getParentFile().mkdirs();
        return outputFile;
    }
}
