package com.techtweakz.cowsandbulls;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

public class PlayerFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);

        listView = view.findViewById(R.id.listViewPlayer);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        return view;
    }

    // Method to set the displayWord before the fragment is created
    public void setDisplayWord(String displayWord) {
        if (adapter != null) {
            updateListViewData(displayWord);
        }
    }

    // Method to update the ListView content
    public void updateListViewData(String data) {
        adapter.add(data); // Append the new word
        adapter.notifyDataSetChanged();
    }
}
