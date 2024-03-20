package com.example.demo;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.RescaleOp;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.AlphaComposite;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.filters.Canvas;
import net.coobird.thumbnailator.filters.Caption;
import net.coobird.thumbnailator.filters.Colorize;
import net.coobird.thumbnailator.filters.ImageFilter;
import net.coobird.thumbnailator.filters.Pipeline;
import net.coobird.thumbnailator.filters.Transparency;
import net.coobird.thumbnailator.geometry.Coordinate;
import net.coobird.thumbnailator.resizers.Resizers;
import org.imgscalr.Scalr;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ImageUtil {
    public static BufferedImage readImage(String inputImagePath) {
        try {
            return ImageIO.read(new File(inputImagePath));
        } catch (IOException e) {
            throw new RuntimeException("Error reading the image", e);
        }
    }

    public static void writeImage(BufferedImage image, String outputImagePath) {
        try {
            ImageIO.write(image, "jpg", new File(outputImagePath));
        } catch (IOException e) {
            throw new RuntimeException("Error writing the image", e);
        }
    }
}