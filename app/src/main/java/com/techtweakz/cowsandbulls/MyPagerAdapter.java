package com.techtweakz.cowsandbulls;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.HashMap;
import java.util.Map;

public class MyPagerAdapter extends FragmentStateAdapter {
    private final Map<String, PlayerFragment> playerFragments = new HashMap<>();
    private int nextPosition = 0; // Track the next available position

    public MyPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void addOrUpdatePlayerTab(String playerName, String displayWord) {
        if (playerFragments.containsKey(playerName)) {
            // Player exists, append the new word
            playerFragments.get(playerName).updateListViewData(displayWord);
        } else {
            // New player, create a new fragment and set the display word
            PlayerFragment playerFragment = new PlayerFragment();
            playerFragment.setDisplayWord(displayWord);
            playerFragments.put(playerName, playerFragment);
            nextPosition++; // Increment for next position
        }

        // Notify that the dataset has changed to refresh the tabs
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return the correct fragment for the player
        String playerName = getPlayerNameAt(position);
        return playerFragments.get(playerName);
    }

    @Override
    public int getItemCount() {
        return playerFragments.size();
    }

    public String getPlayerNameAt(int position) {
        return (String) playerFragments.keySet().toArray()[position]; // Get player name by position
    }
}
