package com.example.sleepy_connect.entrantmanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.sleepy_connect.CommunityCentre;
import com.example.sleepy_connect.CommunityFragment;
import com.example.sleepy_connect.Entrant;
import com.example.sleepy_connect.R;

import java.util.List;

public class EntrantListAdapter extends BaseAdapter {

    private Context context;
    private final List<Entrant> entrants;
    LayoutInflater inflater;

    public EntrantListAdapter(List<Entrant> entrants, Context context) {
        this.entrants = entrants;
        this.context = context;
    }

    public int getCount() {
        return entrants.size();
    }

    public Object getItem(int position) {
        return entrants.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.entrant_list_layout, parent, false);
            holder = new EntrantListAdapter.ViewHolder();
            holder.firstName = convertView.findViewById(R.id.entrant_first_name);
            holder.lastName = convertView.findViewById(R.id.entrant_last_name);
            convertView.setTag(holder);
        } else {
            holder = (EntrantListAdapter.ViewHolder) convertView.getTag();
        }

        Entrant entrant = entrants.get(position);
        holder.firstName.setText(entrant.getFirst_name());
        holder.lastName.setText(entrant.getLast_name());

        return convertView;
    }

    /**
     * class for the items in the holder
     */
    class ViewHolder {
        TextView firstName;
        TextView lastName;
    }
}
