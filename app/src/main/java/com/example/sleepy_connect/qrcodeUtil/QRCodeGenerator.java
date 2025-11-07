package com.example.sleepy_connect.qrcodeUtil;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

// class for generating QR codes
public class QRCodeGenerator {
    Bitmap bitmap;
    BarcodeEncoder barcodeEncoder;


    // default constructor, no parameters yet
    // used in actual code
    public QRCodeGenerator() {
        barcodeEncoder = new BarcodeEncoder();
        return;
    }

    // constructor for injecting mock barcode encoder for testing
    public QRCodeGenerator(BarcodeEncoder barcodeEncoder) {
        this.barcodeEncoder = barcodeEncoder;
    }

    // give the QR code a string to encode and the image view to display the generated code.
    public void generateQRCode(String text, ImageView qrCodeIV) {
        try {

            // creates a Bitmap image of the QR code with a height and width of 400 pixels
            this.bitmap = this.barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 300, 300);

            // sets the Bitmap to ImageView for display to user
            qrCodeIV.setImageBitmap(this.bitmap);
        }

        // in case QR generation fails
        catch (WriterException e) {
            e.printStackTrace();
        }
    }

    // get the generated bitmap
    public Bitmap getBitmap(){
        return this.bitmap;
    }
}
