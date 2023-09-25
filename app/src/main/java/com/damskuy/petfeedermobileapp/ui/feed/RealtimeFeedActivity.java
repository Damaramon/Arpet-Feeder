package com.damskuy.petfeedermobileapp.ui.feed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.NumberPicker;

import com.damskuy.petfeedermobileapp.R;
import com.damskuy.petfeedermobileapp.api.retrofit.RetrofitClient;
import com.damskuy.petfeedermobileapp.data.model.request.RealtimeFeedRequest;
import com.damskuy.petfeedermobileapp.data.model.response.RealtimeFeedResponse;
import com.damskuy.petfeedermobileapp.api.retrofit.RetrofitService;
import com.damskuy.petfeedermobileapp.data.model.Feed;
import com.damskuy.petfeedermobileapp.utils.ViewUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RealtimeFeedActivity extends AppCompatActivity {

    private NumberPicker npServings;
    private Button btnFeed, btnCancel;
    private SweetAlertDialog loadingDialog;
    private final RetrofitService apiService = RetrofitClient.getClient().create(RetrofitService.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime_feed);
        initUI();
        initHandlers();
    }

    private void initUI() {
       npServings = findViewById(R.id.np_servings_realtime);
       btnFeed = findViewById(R.id.btn_feed_realtime);
       btnCancel = findViewById(R.id.btn_cancel_realtime);
       numberPickerConfig();
    }

    private void numberPickerConfig() {
        npServings.setMinValue(Feed.MIN_FEED_AMOUNT);
        npServings.setMaxValue(Feed.MAX_FEED_AMOUNT);
    }

    private void initHandlers() {
        btnFeed.setOnClickListener(v -> {
            Feed feed = new Feed().setFeedAmount(npServings.getValue());
            RealtimeFeedRequest requestData = new RealtimeFeedRequest("592f5ec4-c3dd-4d5c-93fe-4ee3db513ad7", feed);
            Call<RealtimeFeedResponse> apiCaller = apiService.realtimeFeed(requestData);
            loadingDialog = ViewUtils.showLoadingDialog(RealtimeFeedActivity.this);

            apiCaller.enqueue(new Callback<RealtimeFeedResponse>() {
                @Override
                public void onResponse(@NonNull Call<RealtimeFeedResponse> call, @NonNull Response<RealtimeFeedResponse> response) {
                    ViewUtils.hideLoadingDialog(loadingDialog);
                    RealtimeFeedResponse responseData = response.body();
                    if (responseData == null) {
                        ViewUtils.fireErrorAlert(RealtimeFeedActivity.this, "Unable to get response");
                        return;
                    }
                    RealtimeFeedResponse.Data data  = responseData.getData();
                    if (response.isSuccessful()) {
                        ViewUtils.fireSuccessAlert(RealtimeFeedActivity.this, "Fed at : " + data.getCreatedAt());
                    } else {
                        ViewUtils.fireErrorAlert(RealtimeFeedActivity.this, responseData.getError());
                    }
                }
                @Override
                public void onFailure(@NonNull Call<RealtimeFeedResponse> call, @NonNull Throwable t) {
                    ViewUtils.hideLoadingDialog(loadingDialog);
                    ViewUtils.fireErrorAlert(RealtimeFeedActivity.this, "Connection error");
                }
            });
        });

        btnCancel.setOnClickListener(v -> onBackPressed());
    }
}