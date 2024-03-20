package com.example.beautybook.util.impl;

import com.example.beautybook.exceptions.ImageProcessingException;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;

public class ImageUtil {
    private static final String IMAGE_FORMAT = "jpg";
    private static final float IMAGE_QUALITY = 0.0f;
    private static final int ELEMENT_SIZE = 20;

    public static void resizeImage(
            String inputImagePath,
            String outputImagePath,
            int targetWidth,
            int targetHeight
    ) {
        BufferedImage image = readImage(inputImagePath);
        double targetCoefficient = (double) targetWidth / targetHeight;
        double originalCoefficient = (double) image.getWidth() / image.getHeight();
        BufferedImage finalImage;
        if (targetCoefficient > originalCoefficient) {
            int correctHeight =
                    (int) (image.getHeight() / (targetCoefficient / originalCoefficient));
            int y = (Math.abs(image.getHeight() - correctHeight)) / 2;
            finalImage = image.getSubimage(0, y, image.getWidth(), correctHeight);
        } else {
            int correctWidth = (int) (image.getWidth() / (originalCoefficient / targetCoefficient));
            int x = (Math.abs(image.getWidth() - correctWidth)) / 2;
            finalImage = image.getSubimage((int) x, 0, correctWidth, image.getHeight());
        }
        try {
            Thumbnails.of(finalImage)
                    .height(targetHeight)
                    .outputFormat(IMAGE_FORMAT)
                    .imageType(BufferedImage.TYPE_INT_ARGB)
                    .toFile(outputImagePath);
        } catch (IOException e) {
            throw new ImageProcessingException("Error resizing the image", e);
        }
    }

    public static void applyBlur(String inputImagePath, String outputImagePath) {
        BufferedImage image = readImage(inputImagePath);
        try {
            Thumbnails.of(image)
                    .size(
                            image.getWidth() / 2 + ELEMENT_SIZE,
                            image.getHeight() / 2 + ELEMENT_SIZE
                    )
                    .outputFormat(IMAGE_FORMAT)
                    .outputQuality(IMAGE_QUALITY)
                    .imageType(BufferedImage.TYPE_INT_ARGB)
                    .toFile(outputImagePath);
        } catch (IOException e) {
            throw new ImageProcessingException("Error applying blur to the image", e);
        }
        BufferedImageOp op = new ConvolveOp(
                new Kernel(ELEMENT_SIZE, ELEMENT_SIZE, getMatrix()),
                ConvolveOp.EDGE_NO_OP,
                null
        );
        BufferedImage blurredImage = op.filter(readImage(outputImagePath), null);
        BufferedImage finalImage = blurredImage.getSubimage(
                ELEMENT_SIZE / 2,
                ELEMENT_SIZE / 2,
                blurredImage.getWidth() - ELEMENT_SIZE,
                blurredImage.getHeight() - ELEMENT_SIZE
        );
        writeImage(finalImage, outputImagePath);
    }

    private static BufferedImage readImage(String inputImagePath) {
        try {
            return ImageIO.read(new File(inputImagePath));
        } catch (IOException e) {
            throw new ImageProcessingException("Error reading the image", e);
        }
    }

    private static void writeImage(BufferedImage image, String outputImagePath) {
        try {
            ImageIO.write(image, "jpg", new File(outputImagePath));
        } catch (IOException e) {
            throw new ImageProcessingException("Error writing the image", e);
        }
    }

    private static float[] getMatrix() {
        float[] matrix = new float[400];
        for (int i = 0; i < 400; i++) {
            matrix[i] = 1.0f / 500.0f;
        }
        return matrix;
    }
}
