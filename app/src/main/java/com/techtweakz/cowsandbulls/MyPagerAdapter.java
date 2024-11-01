package com.techtweakz.cowsandbulls;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends FragmentStateAdapter {
    private int numPlayers;
    private int tabIndex;
    private String displayWord;
    private List<PlayerFragment> playerFragments;

    public MyPagerAdapter(FragmentActivity fragmentActivity, int numPlayers, int tabIndex, String displayWord) {
        super(fragmentActivity);
        this.numPlayers = numPlayers;
        this.displayWord = displayWord;
        this.tabIndex = tabIndex;
        playerFragments = new ArrayList<>();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (playerFragments.size() > position && playerFragments.get(position) != null) {
            if (position == tabIndex) {
                playerFragments.get(position).updateListViewData(displayWord);
            }
            return playerFragments.get(position);
        } else {
            PlayerFragment playerFragment = new PlayerFragment();
            Bundle args = new Bundle();
            if (position == tabIndex) {
                args.putString("displayWord", displayWord);
            }
            playerFragment.setArguments(args);

            while (playerFragments.size() <= position) {
                playerFragments.add(null);
            }

            playerFragments.set(position, playerFragment);
            return playerFragment;
        }
    }

    @Override
    public int getItemCount() {
        return numPlayers;
    }
}