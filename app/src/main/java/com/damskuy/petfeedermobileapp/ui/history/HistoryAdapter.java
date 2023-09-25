package com.damskuy.petfeedermobileapp.ui.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.damskuy.petfeedermobileapp.R;

import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<HistoryData> {

    public HistoryAdapter(Context context, ArrayList<HistoryData> historyList) {
        super(context, 0, historyList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        HistoryData historyItem = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_list_item, parent, false);
        }

        TextView day = convertView.findViewById(R.id.daydate);
        TextView time = convertView.findViewById(R.id.time);


        day.setText(historyItem.getDay());
        time.setText(historyItem.getTime());

        return convertView;
    }
}
