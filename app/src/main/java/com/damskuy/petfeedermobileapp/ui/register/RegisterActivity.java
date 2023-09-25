package com.damskuy.petfeedermobileapp.ui.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.Toast;

import com.damskuy.petfeedermobileapp.R;
import com.damskuy.petfeedermobileapp.data.model.Result;
import com.damskuy.petfeedermobileapp.data.auth.AuthRepository;
import com.damskuy.petfeedermobileapp.data.model.AuthenticatedUser;
import com.damskuy.petfeedermobileapp.utils.ViewUtils;
import com.damskuy.petfeedermobileapp.ui.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout edtLayoutName, edtLayoutEmail, edtLayoutPassword, edtLayoutConfPassword;
    private TextInputEditText edtName, edtEmail, edtPassword, edtConfPassword;
    private AppCompatButton btnRegister;
    private RegisterViewModel registerViewModel;
    private CheckBox checkBoxAgreement;
    private SweetAlertDialog alertDialog;
    private boolean formValid = false;

    @Override
    protected void onStart() {
        super.onStart();
        if (registerViewModel.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();

        ViewModelProvider registerViewModelProvider =
                new ViewModelProvider(this, new RegisterViewModelFactory(getApplication()));
        registerViewModel = registerViewModelProvider.get(RegisterViewModel.class);

        observeRegisterFormState();
        observeRegisterResult();

        typingInputValidation(edtLayoutName, edtName);
        typingInputValidation(edtLayoutEmail, edtEmail);
        typingInputValidation(edtLayoutPassword, edtPassword);
        typingInputValidation(edtLayoutConfPassword, edtConfPassword);

        btnRegister.setOnClickListener(v -> {
            if (!formValid) {
                notifyRegisterInputChanged();
                ViewUtils.vibratePhone(
                        getApplicationContext(),
                        (Vibrator) getSystemService(Context.VIBRATOR_SERVICE),
                        findViewById(R.id.register_container),
                        500
                );
            } else {
                if (!checkBoxAgreement.isChecked()) {
                    Toast.makeText(this, getString(R.string.validation_agreements), Toast.LENGTH_SHORT).show();
                    return;
                }
                String name = ViewUtils.getEdtText(edtName);
                String email = ViewUtils.getEdtText(edtEmail);
                String password = ViewUtils.getEdtText(edtPassword);
                registerViewModel.register(name, email, password);
                showLoadingDialog();
            }
        });
    }

    private void typingInputValidation(TextInputLayout edtLayout, TextInputEditText edt) {
        Handler handler = new Handler();
        Runnable runnable = this::notifyRegisterInputChanged;
        TextWatcher typingInputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtLayout.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable editable) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 700);
            }
        };
        edt.addTextChangedListener(typingInputWatcher);
    }

    private void notifyRegisterInputChanged() {
        registerViewModel.registerInputChanged(
                ViewUtils.getEdtText(edtName),
                ViewUtils.getEdtText(edtEmail),
                ViewUtils.getEdtText(edtPassword),
                ViewUtils.getEdtText(edtConfPassword)
        );
    }

    private void showLoadingDialog() {
        alertDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        alertDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        alertDialog.setTitleText("Loading");
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    private void hideLoadingDialog() { alertDialog.dismissWithAnimation(); }

    private void observeRegisterFormState() {
        registerViewModel.getRegisterFormState().observe(this, registerFormState -> {
            if (registerFormState.getNameError() == null) edtLayoutName.setErrorEnabled(false);
            else edtLayoutName.setError(registerFormState.getNameError());
            if (registerFormState.getEmailError() == null) edtLayoutEmail.setErrorEnabled(false);
            else edtLayoutEmail.setError(registerFormState.getEmailError());
            if (registerFormState.getPasswordError() == null) edtLayoutPassword.setErrorEnabled(false);
            else edtLayoutPassword.setError(registerFormState.getPasswordError());
            if (registerFormState.getConfPasswordError() == null) edtLayoutConfPassword.setErrorEnabled(false);
            else edtLayoutConfPassword.setError(registerFormState.getConfPasswordError());
            formValid = registerFormState.isFormValid();
        });
    }

    private void observeRegisterResult() {
        registerViewModel.getRegisterResult().observe(this, registerResult -> {
            hideLoadingDialog();
            if (registerResult instanceof Result.Success) {
                AuthenticatedUser user = ((Result.Success<AuthenticatedUser>) registerResult).getData();
                ViewUtils.fireSuccessAlert(this, "Successfully Register");
                AuthRepository.getInstance().createUserSession(RegisterActivity.this, user);
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            } else {
                String error = ((Result.Error<AuthenticatedUser>) registerResult).getErrorMessage();
                ViewUtils.fireErrorAlert(this, error);
            }
        });
    }

    private void initUI() {
        edtName = findViewById(R.id.edt_name_register);
        edtLayoutName = findViewById(R.id.edt_layout_name_register);
        edtEmail = findViewById(R.id.edt_email_register);
        edtLayoutEmail = findViewById(R.id.edt_layout_email_register);
        edtPassword = findViewById(R.id.edt_password_register);
        edtLayoutPassword = findViewById(R.id.edt_layout_password_register);
        edtConfPassword = findViewById(R.id.edt_conf_password_register);
        edtLayoutConfPassword = findViewById(R.id.edt_layout_conf_password_register);
        btnRegister = findViewById(R.id.btn_register);
        checkBoxAgreement = findViewById(R.id.checkbox_agreement_register);
        textInputConfiguration();
    }

    private void textInputConfiguration() {
        String edtNameHint = getString(R.string.name_edt_placeholder);
        String edtEmailHint = getString(R.string.email_edt_placeholder);
        String edtPasswordHint = getString(R.string.password_edt_placeholder);

        edtName.setOnFocusChangeListener((v, hasFocus) ->
                ViewUtils.hideTextInputHint(hasFocus, edtName, edtLayoutName, edtNameHint));

        edtEmail.setOnFocusChangeListener((v, hasFocus) ->
                ViewUtils.hideTextInputHint(hasFocus, edtEmail, edtLayoutEmail, edtEmailHint));

        edtPassword.setOnFocusChangeListener((v, hasFocus) ->
                ViewUtils.hideTextInputHint(hasFocus, edtPassword, edtLayoutPassword, edtPasswordHint));

        edtConfPassword.setOnFocusChangeListener(((v, hasFocus) ->
                ViewUtils.hideTextInputHint(hasFocus, edtConfPassword, edtLayoutConfPassword, edtPasswordHint)));
    }
}