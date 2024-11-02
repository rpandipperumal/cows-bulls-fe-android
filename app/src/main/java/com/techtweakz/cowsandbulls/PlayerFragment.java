package com.techtweakz.cowsandbulls;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class PlayerFragment extends Fragment {

    private List<String> displayWords = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    // Add a word to the list and update the view
    public void updateListViewData(String displayWord) {
        displayWords.add(displayWord);
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    // Optional: Initial word setup for the fragment
    public void setDisplayWord(String displayWord) {
        displayWords.clear();
        displayWords.add(displayWord);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        ListView listView = rootView.findViewById(R.id.myListView);

        // Initialize adapter for ListView
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, displayWords);
        listView.setAdapter(adapter);

        // Set headers for the ListView
        View header = inflater.inflate(R.layout.row_layout, listView, false);
        listView.addHeaderView(header);

        return rootView;
    }
}
