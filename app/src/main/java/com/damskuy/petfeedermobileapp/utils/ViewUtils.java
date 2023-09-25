package com.damskuy.petfeedermobileapp.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

import com.damskuy.petfeedermobileapp.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ViewUtils {

    public static void hideTextInputHint(
            boolean focus,
            EditText edt,
            TextInputLayout edtLayout,
            String hint
    ) {
        if (focus) edtLayout.setHint(null);
        else {
            if (TextUtils.isEmpty(edt.getText())) edtLayout.setHint(hint);
            else edtLayout.setHint(null);
        }
    }

    public static String getEdtText(EditText edt) {
        return Objects.requireNonNull(edt.getText()).toString();
    }

    public static void vibratePhone(Context context, Vibrator service, View parent, int duration) {
        Animation shakeAnimation = AnimationUtils.loadAnimation(context, R.anim.shake_animation);
        parent.startAnimation(shakeAnimation);
        service.vibrate(duration);
        new Handler().postDelayed(service::cancel, duration);
    }

    public static void fireSuccessAlert(Context context, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success!")
                .setContentText(message)
                .show();
    }

    public static void fireErrorAlert(Context context, String message) {
        new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(message)
                .show();
    }

    public static SweetAlertDialog showLoadingDialog(Context context) {
        SweetAlertDialog dialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        dialog.setTitleText("Loading");
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }

    public static void hideLoadingDialog(SweetAlertDialog dialog) { dialog.dismissWithAnimation(); }
}
