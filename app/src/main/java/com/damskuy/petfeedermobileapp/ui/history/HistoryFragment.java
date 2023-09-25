package com.damskuy.petfeedermobileapp.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.damskuy.petfeedermobileapp.R;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private ListView historyListView;
    private HistoryAdapter historyListAdapter;
    private ArrayList<HistoryData> historyList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        // Initialize historyList
        historyList = new ArrayList<>();
        historyList.add(new HistoryData("Wednesday, 22-02-2023", "\t10:00 AM - 5 rounds\n\t04:00 PM - 5 rounds\n\t09:00 PM - 5 rounds\n"));
        historyList.add(new HistoryData("Thursday, 23-02-2023", "\t10:00 AM - 5 rounds\n\t02:00 PM - 3 rounds\n\t04:00 PM - 5 rounds\n\t09:00 PM - 5 rounds\n"));
        historyList.add(new HistoryData("Friday, 24-02-2023", "\t10:00 AM - 5 rounds\n\t04:00 PM - 5 rounds\n\t09:00 PM - 5 rounds\n"));
        historyList.add(new HistoryData("Saturday, 25-02-2023", "\t10:00 AM - 5 rounds\n\t04:00 PM - 5 rounds\n\t07:00 PM - 7 rounds\n"));
        historyList.add(new HistoryData("Sunday, 26-02-2023", "\t10:00 AM - 5 rounds\n\t03:00 PM - 3 rounds\n\t09:00 PM - 5 rounds\n"));

        // Initialize historyListView and set adapter
        historyListView = view.findViewById(R.id.history_list_view);
        historyListAdapter = new HistoryAdapter(view.getContext(), historyList);
        historyListView.setAdapter(historyListAdapter);

        return view;
    }
}