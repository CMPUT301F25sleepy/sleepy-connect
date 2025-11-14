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

    private final Uri imageUri;
    private final String base64String;

    public Image(Context context, Uri uri) throws IOException {                 // Android really really likes this IOexception
        this.imageUri = uri;
        this.base64String = encodeImage(context, uri);
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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
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

    public Uri getImageUri() {
        return imageUri;
    }

    public String getBase64String() {
        return base64String;
    }
}
