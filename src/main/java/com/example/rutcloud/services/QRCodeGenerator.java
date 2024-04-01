package com.example.rutcloud.services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Component
public class QRCodeGenerator {

    public byte[] generateQRCode(String uri) throws WriterException, IOException {
        int width = 300;
        int height = 300;

        // Установка параметров кодирования QR-кода
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        // Создание объекта MultiFormatWriter для генерации QR-кода
        MultiFormatWriter writer = new MultiFormatWriter();

        // Генерация QR-кода
        BitMatrix bitMatrix = writer.encode(uri, BarcodeFormat.QR_CODE, width, height, hints);

        // Конвертация QR-кода в изображение
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return outputStream.toByteArray();
    }
}