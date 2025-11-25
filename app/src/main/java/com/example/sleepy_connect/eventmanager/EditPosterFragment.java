package com.example.sleepy_connect.eventmanager;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sleepy_connect.Event;
import com.example.sleepy_connect.EventDAL;
import com.example.sleepy_connect.Image;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.EventViewModel;

import java.io.IOException;

/**
 * Fragment class for editing created event's poster.
 * @author Sam Francisco
 */
public class EditPosterFragment extends Fragment {

    private Event event;
    private Uri posterUri;
    private ImageView ivPoster;

    // Registers a photo picker activity launcher in single-select mode.
    private final ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                    posterUri = uri;
                    ivPoster.setImageURI(uri);

                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    public EditPosterFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method for this fragment.
     * @return A new instance of fragment EditEventFragment.
     */
    public static EditPosterFragment newInstance() {
        return new EditPosterFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // change toolbar title
        TextView title = requireActivity().findViewById(R.id.set_title);
        title.setText("Edit Poster");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_poster, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get event from viewmodel
        EventViewModel vmEvent = new ViewModelProvider(requireActivity()).get(EventViewModel.class);
        event = vmEvent.getEvent().getValue();
        assert event != null;

        // set poster if provided
        ivPoster = view.findViewById(R.id.edit_poster_iv_poster);
        if (event.getPoster() != null) {
            Image img = new Image(event.getPoster());
            ivPoster.setImageBitmap(img.decodeImage());
        }

        // set poster imageview's listener
        ivPoster.setOnClickListener(v -> pickMedia.launch(
                new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build()
        ));

        // Save event button
        TextView saveBtn = view.findViewById(R.id.edit_poster_tv_confirm);
        saveBtn.setOnClickListener(v -> saveEvent());
    }

    /**
     * Saves new poster if updated, otherwise nothing is saved
     */
    private void saveEvent() {

        // update poster if changed
        if (posterUri != null) {
            try {
                Image img = new Image(getContext(), posterUri);
                event.setPoster(img.getBase64String());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // update database
        EventDAL dal = new EventDAL();
        dal.updateEvent(event);

        // pop fragment from backstack (return)
        requireActivity().getSupportFragmentManager()
                .popBackStack();
    }
}