package com.bookstore.utils.imageLoader;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Service

public class ImageResizeService {
    public void saveResizedImg(String url,
                               byte[] imageBytes) throws IOException {
        try {
            BufferedImage original = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (original == null) {
                System.out.println("Unsupported or broken image format for URL: " + url);
                return;
            }
            int width = original.getWidth();
            int height = original.getHeight();

            if ((width / 2) == 0 || (height / 2) == 0) {
                System.out.println("Image width or height should not be zero " + url);
                return;
            }

            BufferedImage resizedImp = resizeImg(original,
                    width / 2,
                    width / 2);

            File resizedFile = getFile(url, "resized");
            if (!ImageIO.write(resizedImp, "jpg", resizedFile)) {
                System.out.println("Filed to save resized image");
            }
        } catch (IOException |IllegalArgumentException exception) {
            System.out.println("Error resizing image for URL: " + url);
            exception.printStackTrace();
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
        String ImageFilePath = view.getHomeDirectory().getPath() + "/Workspace/ImagesBook/" + directory;
        File outputFile = new File(ImageFilePath + "/" +
                urlParts[urlParts.length - 2] + "/" +
                urlParts[urlParts.length - 1]);
        outputFile.getParentFile().mkdirs();
        return outputFile;
    }
}
