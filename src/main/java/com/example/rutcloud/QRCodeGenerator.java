package com.example.rutcloud;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class QRCodeGenerator {
    public static byte[] generateQRCodeImage(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageConfig config = new MatrixToImageConfig(0xFF0000, 0xFFFFFFFF);
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream, config);

        return pngOutputStream.toByteArray();
    }

    public static void saveQRCodeToFile(byte[] imageData, String fileName) throws IOException {
        Path path = FileSystems.getDefault().getPath(fileName);
        Files.write(path, imageData);
    }
}