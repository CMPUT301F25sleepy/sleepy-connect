package com.example.sleepy_connect;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.sleepy_connect.qrcodeUtil.QRCodeGenerator;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@RunWith(AndroidJUnit4.class)
public class QRCodeGeneratorTest {

    @Mock
    BarcodeEncoder mockEncoder;

    @Mock
    ImageView mockImageView;

    QRCodeGenerator generator;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        generator = new QRCodeGenerator(mockEncoder);
    }

    @Test
    public void testGenerateQRCodeGood() throws Exception {
        // make bitmap
        Bitmap bitmap = Bitmap.createBitmap(20, 20, Bitmap.Config.ARGB_8888);

        // mock encoder
        when(mockEncoder.encodeBitmap(anyString(), any(), anyInt(), anyInt())).thenReturn(bitmap);

        generator.generateQRCode("1", mockImageView);

        // check the call to encoder
        verify(mockEncoder).encodeBitmap(
                eq("sleepyEventApp/1"), eq(BarcodeFormat.QR_CODE), eq(300), eq(300)
        );

        // check if bitmap is in image view
        verify(mockImageView).setImageBitmap(bitmap);

        // check the getter
        assertEquals(bitmap, generator.getBitmap());
    }
}