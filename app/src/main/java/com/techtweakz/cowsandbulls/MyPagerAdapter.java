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

public class MyPagerAdapter extends FragmentStateAdapter {
    private int numPlayers;
    private String displayWord;

    public MyPagerAdapter(FragmentActivity fragmentActivity, int numPlayers, String displayWord) {
        super(fragmentActivity);
        this.numPlayers = numPlayers;
        this.displayWord = displayWord;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        PlayerFragment playerFragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putString("displayWord", displayWord);
        playerFragment.setArguments(args);
        return playerFragment;
    }

    @Override
    public int getItemCount() {
        return numPlayers;
    }
}
