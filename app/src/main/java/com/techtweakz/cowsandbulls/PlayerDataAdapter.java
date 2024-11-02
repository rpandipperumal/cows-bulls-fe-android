package com.techtweakz.cowsandbulls;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class PlayerDataAdapter extends BaseAdapter {

    private final Context context;
    private final List<String> displayWords;

    public PlayerDataAdapter(Context context, List<String> displayWords) {
        this.context = context;
        this.displayWords = displayWords;
    }

    @Override
    public int getCount() {
        return displayWords.size();
    }

    @Override
    public Object getItem(int position) {
        return displayWords.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);
        }

        // Split the displayWord by the separator to get Word, Cows, and Bulls values
        String[] values = displayWords.get(position).split("\\|");

        TextView wordView = convertView.findViewById(R.id.wordView);
        TextView cowsView = convertView.findViewById(R.id.cowsView);
        TextView bullsView = convertView.findViewById(R.id.bullsView);

        wordView.setText(values[0].trim());
        cowsView.setText(values[1].trim());
        bullsView.setText(values[2].trim());

        return convertView;
    }
}
