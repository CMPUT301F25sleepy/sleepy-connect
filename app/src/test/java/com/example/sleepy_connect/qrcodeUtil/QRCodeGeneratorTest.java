package com.example.sleepy_connect.qrcodeUtil;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import android.graphics.Bitmap;
import android.widget.ImageView;

// Imports for decoding the QR Code
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Local unit test for the QRCodeGenerator class.
 * This test verifies that the generated QR code bitmap contains the correct data.
 */
@RunWith(MockitoJUnitRunner.class) // Use Mockito's runner to handle mocks
public class QRCodeGeneratorTest {

    private QRCodeGenerator qrCodeGenerator;

    // Mock the ImageView because it's an Android UI component
    @Mock
    ImageView mockImageView;

    // fake barcode encoder
    @Mock
    private BarcodeEncoder mockBarcodeEncoder;

    // A fake Bitmap
    @Mock
    private Bitmap mockBitmap;


    @Before
    public void setUp() {
        // generate test qrCodeGenerator with mocked barcode encoder
        qrCodeGenerator = new QRCodeGenerator(mockBarcodeEncoder);
    }

    // test qrCode generation with 1 character
    @Test
    public void testGenerate_withSingleCharacter() throws Exception {
        String testString = "A";

        // Define the behavior of the mock encoder:
        // "When createBitmap is called with ANY BitMatrix, return our fake mockBitmap"
        when(mockBarcodeEncoder.createBitmap(any(BitMatrix.class))).thenReturn(mockBitmap);

        qrCodeGenerator.generateQRCode(testString, mockImageView);

        // Was the createBitmap method called 1 time?
        verify(mockBarcodeEncoder, times(1)).createBitmap(any(BitMatrix.class));

        // Was the setImageBitmap on mock ImageView called with fake bitmap?
        verify(mockImageView, times(1)).setImageBitmap(mockBitmap);

        // Is bitmap in generator the one?
        assertEquals("The stored bitmap should be the one from the mock encoder", mockBitmap, qrCodeGenerator.getBitmap());
    }

    @Test
    public void testGenerate_withLongString() throws Exception {
        String longTestString = "This is a very long string used to test the QR code generator's ability...";

        // Set up the mock's behavior, same as before
        when(mockBarcodeEncoder.createBitmap(any(BitMatrix.class))).thenReturn(mockBitmap);

        // We can use an ArgumentCaptor to "capture" the string passed to the encoder
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        qrCodeGenerator.generateQRCode(longTestString, mockImageView);

        // Verify that the encode() method (which happens before createBitmap) was called.
        // Then, capture the string that was passed to it.
        verify(mockBarcodeEncoder).encode(stringCaptor.capture(), eq(BarcodeFormat.QR_CODE), anyInt(), anyInt());

        // Check if the captured string matches our original long string.
        // This confirms the correct data was passed to the encoder!
        Assert.assertEquals(longTestString, stringCaptor.getValue());

        // Also verify the ImageView was updated
        verify(mockImageView, times(1)).setImageBitmap(mockBitmap);
    }

    @Test
    public void testGenerate_withEmptyString() throws Exception {
        String testString = "";

        qrCodeGenerator.generateQRCode(testString, mockImageView);

        // Verify that NEITHER encode() NOR createBitmap() were ever called on the encoder
        verify(mockBarcodeEncoder, never()).encode(anyString(), any(), anyInt(), anyInt());
        verify(mockBarcodeEncoder, never()).createBitmap(any());

        // Verify that the ImageView was never touched
        verify(mockImageView, never()).setImageBitmap(any());

        // Assert that the internal bitmap in the generator is null
        assertNull("Bitmap should be null for an empty input string", qrCodeGenerator.getBitmap());
    }

    @Test
    public void testGenerate_withNullString() throws Exception {
        // This test is very similar to the empty string test

        String testString = null;

        qrCodeGenerator.generateQRCode(testString, mockImageView);

        // Verify that the encoder and ImageView were never called
        verify(mockBarcodeEncoder, never()).encode(any(), any(), anyInt(), anyInt());
        verify(mockBarcodeEncoder, never()).createBitmap(any());
        verify(mockImageView, never()).setImageBitmap(any());

        assertNull("Bitmap should be null for a null input string", qrCodeGenerator.getBitmap());
    }
}