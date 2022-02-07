package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {

    private int maxWidth;
    private int maxHeight;
    private double maxRatio;
    private TextColorSchema schema;

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));  // Получаем изображение по URL
        int newWidth = img.getWidth();
        int newHeight = img.getHeight();
        double ratio = (double) newWidth / newHeight;

        //Проверяем на соотношение сторон
        if (ratio > maxRatio && maxRatio > 0) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

//      Значения установлены сервером:
//      this.converter.setMaxHeight(300);
//      this.converter.setMaxWidth(300);
//      this.converter.setMaxRatio(4);

        if (maxHeight < newHeight && maxHeight > 0) {
            double coefficient = (double) newHeight / (double) maxHeight;
            newHeight = maxHeight;
            newWidth = (int) (newWidth / coefficient);
        }
        if (maxWidth < newWidth && maxWidth > 0) {
            double coefficient = (double) newWidth / (double) maxWidth;
            newWidth = maxWidth;
            newHeight = (int) (newHeight / coefficient);
        }

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);  // сужаем старую картинку
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);  // делаем ч/б картинку
        Graphics2D graphics = bwImg.createGraphics();  // инструмент для рисования
        graphics.drawImage(scaledImage, 0, 0, null);  // копируем содержимое из суженой картинки

        StringBuilder sb = new StringBuilder();
        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                WritableRaster bwRaster = bwImg.getRaster();
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                sb.append(c);
                sb.append(c);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}