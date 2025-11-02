package com.example.sleepy_connect.qrcodeUtil;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCodeGenerator {
    // give the QR code a string to encode and the image view to display the generated code.
    public void generateQRCode(String text, ImageView qrCodeIV) {
        BarcodeEncoder barcodeEncoder
                = new BarcodeEncoder();
        try {

            // This method returns a Bitmap image of the
            // encoded text with a height and width of 400
            // pixels.
            Bitmap bitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 400, 400);

            // Sets the Bitmap to ImageView
            qrCodeIV.setImageBitmap(bitmap);
        }
        catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
