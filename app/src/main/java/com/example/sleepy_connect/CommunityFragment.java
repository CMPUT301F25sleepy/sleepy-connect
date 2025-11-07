package com.example.sleepy_connect;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class CommunityFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ListView listView;
    private CommunityCentreAdapter adapter;
    private final List<CommunityCentre> centreList = new ArrayList<>();

    public CommunityFragment() {
        // Required empty public constructor
    }

    public static CommunityFragment newInstance(String param1, String param2) {
        CommunityFragment fragment = new CommunityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_display, container, false);

        listView =view.findViewById(R.id.list_view);
        adapter = new CommunityCentreAdapter(centreList);
        listView.setAdapter(adapter);

        // Fetch data from Firestore
        CommunityCentreDAL dal = new CommunityCentreDAL();
        dal.getCommunityCentres(new CommunityCentreDAL.OnCommunityCentresRetrievedListener() {
            @Override
            public void onCommunityCentresRetrieved(List<CommunityCentre> centres) {
                centreList.clear();
                if (centres == null || centres.isEmpty()) {
                    // Add placeholder if no centres
                    CommunityCentre placeholder = new CommunityCentre();
                    placeholder.communityCentreName = "No community centres available";
                    placeholder.communityCentreLocation = "";
                    centreList.add(placeholder);
                } else {
                    centreList.addAll(centres);
                }
                requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
            }
        });

        listView.setOnItemClickListener((parent, view1, position, id) -> {
            TextView locationName = view1.findViewById(R.id.alert_message);
            String clickedLocationName = locationName.getText().toString();

            EventListFragment eventListFrag = EventListFragment.newInstance(clickedLocationName);
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, eventListFrag)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }



    // Adapter for Amelia's Fancy ListView
    private class CommunityCentreAdapter extends BaseAdapter {

        private final List<CommunityCentre> centres;

        public CommunityCentreAdapter(List<CommunityCentre> centres) {
            this.centres = centres;
        }

        @Override
        public int getCount() {
            return centres.size();
        }

        @Override
        public Object getItem(int position) {
            return centres.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.location_item_layout, parent, false);
                holder = new ViewHolder();
                holder.name = convertView.findViewById(R.id.alert_message);
                holder.address = convertView.findViewById(R.id.location_address);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            CommunityCentre centre = centres.get(position);
            holder.name.setText(centre.getCommunityCentreName());
            holder.address.setText(centre.getCommunityCentreLocation());

            return convertView;
        }

        class ViewHolder {
            TextView name;
            TextView address;
        }
    }
}