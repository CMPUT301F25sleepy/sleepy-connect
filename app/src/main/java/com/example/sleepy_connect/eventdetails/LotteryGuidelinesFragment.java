package com.example.sleepy_connect.eventdetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sleepy_connect.R;

/**
 * Fragment class that displays lottery guidelines accessed through the event details
 */
public class LotteryGuidelinesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lottery_guidelines, container, false);

        Button closeFragmentButton = view.findViewById(R.id.guidelines_close_button);

        closeFragmentButton.setOnClickListener(v -> closeFragment());

        return view;
    }

    /**
     * Closes the fragment after user clicks "Got it!"
     */
    private void closeFragment() {
        requireParentFragment().getChildFragmentManager()
            .beginTransaction()
            .remove(this)
            .commit();

    }
}
