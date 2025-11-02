package com.example.sleepy_connect;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sleepy_connect.qrcodeUtil.QRCodeGenerator;

public class TestingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testing_activity);

        // initialize QR code generator
        QRCodeGenerator myGenerator = new QRCodeGenerator();

        // Initialize UI components
        ImageView qrCodeIV = findViewById(R.id.imageView2);
        Button generateQRCodeButton = findViewById(R.id.button2);

        // set onclick listener for button
        generateQRCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myGenerator.generateQRCode("https://www.google.com", qrCodeIV);
            }
        });

    }
}
