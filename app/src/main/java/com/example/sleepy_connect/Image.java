package com.example.sleepy_connect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Image {

    private final Uri imageUri;
    private final String base64String;

    public Image(Context context, Uri uri) throws IOException {                 // Android really really likes this IOexception stuff
        this.imageUri = uri;
        this.base64String = encodeImage(context, uri);
    }

    private String encodeImage(Context context, Uri uri) throws IOException {
        // Encodes the image from uri to String to store into Firebase
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public Bitmap decodeImage() {
        // Decodes the image after getting it from Firebase
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
