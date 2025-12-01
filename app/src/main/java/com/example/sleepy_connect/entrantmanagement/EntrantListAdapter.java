package com.example.sleepy_connect.entrantmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.EntrantDAL;
import com.example.sleepy_connect.R;

import java.util.ArrayList;
import java.util.List;

/**
 * custom list adapter class for the list of profiles in the organizer view
 */
public class EntrantListAdapter extends BaseAdapter {

    private Context context;
    private final List<String> entrantsID;
    private final List<Entrant> entrants = new ArrayList<>();
    private LayoutInflater inflater;

    public EntrantListAdapter(ArrayList<String> entrantsID, Context context) {
        this.entrantsID = entrantsID;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

        EntrantDAL dal = new EntrantDAL();

        // Load each entrant by ID
        for (String entrantID : entrantsID) {
            dal.getEntrant(entrantID, new EntrantDAL.OnEntrantRetrievedListener() {
                @Override
                public void onEntrantRetrieved(Entrant entrant) {
                    if (entrant != null) {
                        entrants.add(entrant);
                        notifyDataSetChanged(); // Refresh the list when entrant loads
                    }
                }
            });
        }
    }

    @Override
    public int getCount() {
        return entrants.size();
    }

    @Override
    public Object getItem(int position) {
        return entrants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.entrant_list_layout, parent, false);
            holder = new ViewHolder();
            holder.firstName = convertView.findViewById(R.id.entrant_first_name);
            holder.lastName = convertView.findViewById(R.id.entrant_last_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Entrant entrant = entrants.get(position);

        holder.firstName.setText(entrant.getFirst_name() != null ? entrant.getFirst_name() : "");
        holder.lastName.setText(entrant.getLast_name() != null ? entrant.getLast_name() : "");

        return convertView;
    }

    /**
     * class for the items in the holder
     */
    static class ViewHolder {
        TextView firstName;
        TextView lastName;
    }
}
