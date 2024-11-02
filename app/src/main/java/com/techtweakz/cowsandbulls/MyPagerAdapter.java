package com.techtweakz.cowsandbulls;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.HashMap;
import java.util.Map;

public class MyPagerAdapter extends FragmentStateAdapter {
    private final Map<String, PlayerFragment> playerFragments = new HashMap<>();
    private int nextPosition = 0;

    public MyPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void addOrUpdatePlayerTab(String playerName, String displayWord) {
        if (playerFragments.containsKey(playerName)) {
            // Player exists, update the existing fragment's data
            playerFragments.get(playerName).updateListViewData(displayWord);
        } else {
            // Create a new fragment for a new player
            PlayerFragment playerFragment = new PlayerFragment();
            playerFragment.setDisplayWord(displayWord);
            playerFragments.put(playerName, playerFragment);
            nextPosition++;
        }

        // Refresh the tabs
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String playerName = getPlayerNameAt(position);
        return playerFragments.get(playerName);
    }

    @Override
    public int getItemCount() {
        return playerFragments.size();
    }

    public String getPlayerNameAt(int position) {
        return (String) playerFragments.keySet().toArray()[position];
    }
}
