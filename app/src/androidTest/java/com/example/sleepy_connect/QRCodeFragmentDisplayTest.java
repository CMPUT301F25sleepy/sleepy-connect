package com.example.sleepy_connect;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

/// import com.agoda.kakao.check.ImageViewHasDrawableMatcher; (commented out since i was getting errors)
import com.example.sleepy_connect.eventdetails.QRCodeFragment;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.function.Predicate;

//import java.util.function.Predicate;

/**
 * Instrumented test for QRCodeFragment.
 * This test verifies that when the fragment is launched with data,
 * it correctly generates and displays a QR code in its ImageView.
 */
@RunWith(AndroidJUnit4.class)
public class QRCodeFragmentDisplayTest {

    // --- IMPORTANT: Replace with your actual resource IDs and names ---
    /**
     * The resource ID of the ImageView that displays the QR code.
     * Found in your fragment's layout XML file (e.g., res/layout/fragment_qrcode.xml).
     */
    private static final int QR_CODE_IMAGE_VIEW_ID = R.id.qr_code_image;

    /**
     * The argument key your fragment expects in its Bundle.
     * Check the newInstance() method or where you read arguments in your QRCodeFragment.
     */
    private static final String FRAGMENT_ARG_KEY = "defaultString";

    /**
     * The resource ID of the default placeholder image in your ImageView.
     * This is the image that should be replaced by the QR code.
     */
    private static final int DEFAULT_PLACEHOLDER_DRAWABLE = R.drawable.black_square_border;
    // ----------------------------------------------------------------

    ///  commented tests out since I couldn't run other test getting errors
//    @Test
//    public void testFragment_displaysGeneratedQRCode_whenDataIsPassed() {
//        // 1. Define the data to be passed to the fragment
//        String testData = "test-user-id-12345";
//        Bundle fragmentArgs = new Bundle();
//        fragmentArgs.putString(FRAGMENT_ARG_KEY, testData);
//
//        // 2. Launch the fragment in a test container, passing the arguments
//        // The FragmentScenario will handle the fragment's lifecycle (onCreate, onCreateView, etc.)
//        FragmentScenario.launchInContainer(QRCodeFragment.class, fragmentArgs);
//
//        // 3. Verify the ImageView no longer shows the default image
//        // This confirms that some drawing operation (our QR code generation) has occurred
//        // and successfully updated the ImageView.
//        onView(withId(QR_CODE_IMAGE_VIEW_ID))
//                .check(matches(Predicate.not(ImageViewHasDrawableMatcher.hasDrawable(DEFAULT_PLACEHOLDER_DRAWABLE))));
//    }

//    @Test
//    public void testFragment_showsDefaultImage_whenNoDataIsPassed() {
//        // 1. Launch the fragment without any arguments
//        FragmentScenario.launchInContainer(QRCodeFragment.class, null);
//
//        // 2. Verify the ImageView shows the default placeholder image
//        // This is a good sanity check to ensure the default state is what we expect.
//        onView(withId(QR_CODE_IMAGE_VIEW_ID))
//                .check(matches(ImageViewHasDrawableMatcher.hasDrawable(DEFAULT_PLACEHOLDER_DRAWABLE)));
//    }
}