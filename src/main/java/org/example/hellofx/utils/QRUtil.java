package org.example.hellofx.utils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QRUtil {

    public static Image generateQRCode(String content, int width, int height) throws WriterException {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix matrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height);
            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
            return SwingFXUtils.toFXImage(bufferedImage, null);
        }
        catch(WriterException we) {
            return null;
        }
    }
}

