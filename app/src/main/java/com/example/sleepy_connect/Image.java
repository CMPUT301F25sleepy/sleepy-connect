package com.example.sleepy_connect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * image object class for the event posters
 */
public class Image {
    private final String base64String;

    public Image(Context context, Uri uri) throws IOException {                 // Android really really likes this IOexception
        this.base64String = encodeImage(context, uri);
    }

    public Image(String base64String) {
        this.base64String = base64String;
    }

    /**
     * Encodes the image from uri to String to store into Firebase
     * @param context context
     * @param uri Uri object to encode
     * @return string of encoded image
     * @throws IOException input output exception for incompletion
     */
    private String encodeImage(Context context, Uri uri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        bitmap = rescaleImage(bitmap);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    /**
     * Decodes the image after getting it from Firebase
     * @return bitmap
     */
    public Bitmap decodeImage() {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    /**
     * Rescales an image so its bitmap is 1024x768 or smaller.
     * @param bmp Bitmap representation of an image
     * @return Scaled Bitmap of an image
     */
    public Bitmap rescaleImage(Bitmap bmp) {
        float ratio = Math.min((float)1024 / bmp.getWidth(), (float)768 / bmp.getHeight());
        int width = Math.round(bmp.getWidth() * ratio);
        int height = Math.round(bmp.getHeight() * ratio);
        return Bitmap.createScaledBitmap(bmp, width, height, true);
    }

    public String getBase64String() {
        return base64String;
    }
}
