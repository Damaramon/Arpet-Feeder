package com.damskuy.petfeedermobileapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.damskuy.petfeedermobileapp.R;
import com.damskuy.petfeedermobileapp.ui.feed.RealtimeFeedActivity;
import com.damskuy.petfeedermobileapp.data.auth.AuthRepository;

public class HomeFragment extends Fragment {

    private Button btnFeed;
    private View parent;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        parent = inflater.inflate(R.layout.home_layout, container, false);
        initUI();
        initHandlers();
        return parent;
    }

    private void initUI() {
        TextView txtUserGreet = parent.findViewById(R.id.txt_user_greet_home);
        txtUserGreet.setText(getString(
                R.string.home_user_greet,
                AuthRepository.getInstance().getAuthenticatedUser().getName()
        ));
        btnFeed = parent.findViewById(R.id.btn_feed_home);
    }

    private void initHandlers() {
        btnFeed.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RealtimeFeedActivity.class);
            startActivity(intent);
        });
    }
}
