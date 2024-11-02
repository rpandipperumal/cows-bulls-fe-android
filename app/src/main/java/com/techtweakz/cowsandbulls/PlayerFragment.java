package com.techtweakz.cowsandbulls;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.techtweakz.cowsandbulls.PlayerDataAdapter;
import com.techtweakz.cowsandbulls.R;

import java.util.ArrayList;
import java.util.List;

public class PlayerFragment extends Fragment {

    private List<String> displayWords = new ArrayList<>();
    private PlayerDataAdapter adapter;

    // Method to add a new item to the ListView
    public void updateListViewData(String displayWord) {
        displayWords.add(displayWord); // Append the new word entry
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // Notify adapter to update the ListView
        }
    }

    // Method to clear the list and set a single display word
    public void setDisplayWord(String displayWord) {
        displayWords.clear(); // Clear previous entries
        displayWords.add(displayWord); // Set the new display word
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_player, container, false);

        ListView listView = rootView.findViewById(R.id.myListView);

        // Initialize the custom adapter with the displayWords list
        adapter = new PlayerDataAdapter(requireContext(), displayWords);
        listView.setAdapter(adapter);

        // Inflate and add the header view
        View header = inflater.inflate(R.layout.row_layout, listView, false);
        listView.addHeaderView(header);

        return rootView;
    }
}
