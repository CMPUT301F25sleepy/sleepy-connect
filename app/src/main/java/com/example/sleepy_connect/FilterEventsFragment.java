package com.example.sleepy_connect;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.search.SearchBar;

public class FilterEventsFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_filter_events, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view);
        Dialog dialog = builder.create();

        CheckBox monday = view.findViewById(R.id.monday_check);
        CheckBox tuesday = view.findViewById(R.id.tuesday_check);
        CheckBox wednesday = view.findViewById(R.id.wednesday_check);
        CheckBox thursday = view.findViewById(R.id.thursday_check);
        CheckBox friday = view.findViewById(R.id.friday_check);
        CheckBox saturday = view.findViewById(R.id.saturday_check);
        CheckBox sunday = view.findViewById(R.id.sunday_check);

        SearchView searchInput = view.findViewById(R.id.filter_search_bar);
        FlexboxLayout keywordContainer = view.findViewById(R.id.keyword_container);

        searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                String keyword = query.trim();
                if (!keyword.isEmpty()) {
                    addChip(keyword, keywordContainer);
                    searchInput.setQuery("", false); // clear the text
                    searchInput.clearFocus();        // close keyboard
                }
                return true;
            }
        });

        return dialog;
    }

        private void addChip(String keyword, FlexboxLayout keywordContainer) {
            Chip chip = new Chip(requireContext());
            chip.setText(keyword);

            chip.setCloseIconVisible(true);
            chip.setChipBackgroundColorResource(
                    com.google.android.material.R.color.material_dynamic_neutral50
            );

            chip.setOnCloseIconClickListener(v -> {
                ((ViewGroup) chip.getParent()).removeView(chip);
            });

            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 0, 8, 0); // left, top, right, bottom spacing in pixels
            chip.setLayoutParams(params);

            keywordContainer.addView(chip);
        }
}
