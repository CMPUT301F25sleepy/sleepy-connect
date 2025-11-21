package com.example.sleepy_connect.admin.profilemanagement;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.R;
import com.example.sleepy_connect.UserViewModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

/**
 * Displays all profiles in the app
 */
public class AdminProfileListFragment extends Fragment {

    private ArrayList<Entrant> entrants;

    public AdminProfileListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AdminProfileListFragment.
     */
    public static AdminProfileListFragment newInstance() {
        return new AdminProfileListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initialize list, listview, adapter
        entrants = new ArrayList<>();
        AdminProfileListAdapter adapter = new AdminProfileListAdapter(requireContext(), entrants);
        ListView listView = view.findViewById(R.id.admin_list_lv);
        listView.setAdapter(adapter);

        // initialize listener for admin profile list items
        listView.setOnItemClickListener((parent, v, position, id) -> {

            // store selected entrant in viewmodel
            Entrant selectedEntrant = entrants.get(position);
            UserViewModel vmEntrant = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
            vmEntrant.setUser(selectedEntrant);

            // show entrant details
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.admin_container, AdminProfileDetailsFragment.class, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });

        // get all users
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    // add all queried entrants to cleared entrant list
                    entrants.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        Entrant entrant = doc.toObject(Entrant.class);
                        entrants.add(entrant);
                    }
                    adapter.notifyDataSetChanged();
                });
    }
}