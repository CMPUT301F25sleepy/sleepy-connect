package com.example.sleepy_connect.admin.profilemanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.R;

import java.util.ArrayList;

public class AdminProfileListAdapter extends ArrayAdapter<Entrant> {

    public AdminProfileListAdapter(Context context, ArrayList<Entrant> entrants) {
        super(context, 0, entrants);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // inflate view
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_admin_profile, parent, false);
        }

        // get formatted item data
        Entrant entrant = getItem(position);
        assert entrant != null;
        String name = formatEntrantName(entrant);

        // set entrant name
        TextView tvEntrantName = view.findViewById(R.id.admin_profile_list_entrant_name);
        tvEntrantName.setText(name);

        // set entrant id
        TextView tvEntrantId = view.findViewById(R.id.admin_profile_list_entrant_id);
        tvEntrantId.setText(entrant.getAndroid_id());

        return view;
    }

    private String formatEntrantName(Entrant entrant) {

        String name = "";

        // case: no name given
        if (entrant.getFirst_name() == null && entrant.getLast_name() == null) {
            return name;
        }

        // case: name given
        if (entrant.getFirst_name() != null)
            name = name + entrant.getFirst_name();
        if (entrant.getFirst_name() != null && entrant.getLast_name() != null)
            name = name + " ";
        if (entrant.getLast_name() != null)
            name = name + entrant.getLast_name();

        return name;
    }
}
