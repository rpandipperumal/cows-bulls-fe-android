package com.techtweakz.cowsandbulls;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.Collections;
import java.util.List;

public class PlayerFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        listView = view.findViewById(R.id.listViewPlayer);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        Bundle args = getArguments();
        if (args != null) {
            String displayWord = args.getString("displayWord");
            if (displayWord != null) {
                updateListViewData(displayWord);
            }
        }

            return view;
    }

    // Provide a method to update the ListView content for each player
    public void updateListViewData(String data) {
        adapter.add(data);
        adapter.notifyDataSetChanged();
    }


}

