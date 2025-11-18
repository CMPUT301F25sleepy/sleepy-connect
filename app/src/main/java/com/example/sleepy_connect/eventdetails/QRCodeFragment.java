package com.example.sleepy_connect.eventdetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sleepy_connect.R;
import com.example.sleepy_connect.qrcodeUtil.QRCodeGenerator;

/**
 * Fragment for displaying QR code to user
 */
public class QRCodeFragment extends Fragment {
    // in case data wasn't passed in correctly, use default string
    // later, could raise error message, but fragment should protect from this
    private static final String stringARG = "defaultString";
    private String data;

    /**
     * Factory method for the fragment
     * @param stringToEncode variable for what the QR code will represent
     * @return new instance of the fragment
     */
    public static QRCodeFragment newInstance(String stringToEncode) {
        QRCodeFragment fragment = new QRCodeFragment();
        // retrieve data from bundle
        Bundle args = new Bundle();
        args.putString(stringARG, stringToEncode);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.qr_code_fragment, container, false);

        Button closeFragmentButton = view.findViewById(R.id.qr_code_close_button);
        ImageView qrCodeImage = view.findViewById(R.id.qr_code_image);

        data = getArguments().getString(stringARG);

        closeFragmentButton.setOnClickListener(v -> closeFragment());

        // create QR code generator
        QRCodeGenerator qrCodeGenerator = new QRCodeGenerator();
        // generate QR code, put into image view
        qrCodeGenerator.generateQRCode(data, qrCodeImage);

        return view;
    }

    /**
     * closes fragment after hitting back button
     */
    private void closeFragment() {
        getParentFragmentManager().popBackStack();
    }

}
