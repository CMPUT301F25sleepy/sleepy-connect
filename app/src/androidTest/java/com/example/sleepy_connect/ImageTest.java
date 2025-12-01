package com.example.sleepy_connect;

import static org.junit.Assert.*;
import android.graphics.Bitmap;
import android.util.Base64;
import org.junit.Test;
import java.io.ByteArrayOutputStream;

public class ImageTest {

    // Needs j unit to work
    // Testing the constructor
    @Test
    public void testConstructor() {
        String sample = "a";
        Image image = new Image(sample);
        assertEquals(sample, image.getBase64String());
    }

    // Testing if compression works
    @Test
    public void testRescale() {
        // make a fake bitmap
        Bitmap large = Bitmap.createBitmap(4000, 3000, Bitmap.Config.ARGB_8888);

        Image dummy = new Image("placeholder");
        Bitmap scaled = dummy.rescaleImage(large);

        assertTrue(scaled.getWidth() <= 1024);
        assertTrue(scaled.getHeight() <= 768);
    }

    // Checking if we get a bitmap back
    @Test
    public void testDecode() {
        // make small bitmap
        Bitmap bmp = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);

        // encode on my own
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        String encoded = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        Image image = new Image(encoded);
        Bitmap decoded = image.decodeImage();

        assertNotNull(decoded);
        assertEquals(10, decoded.getWidth());
        assertEquals(10, decoded.getHeight());
    }
}