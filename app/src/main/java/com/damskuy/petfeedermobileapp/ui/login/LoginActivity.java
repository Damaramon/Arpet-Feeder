package com.damskuy.petfeedermobileapp.ui.login;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.damskuy.petfeedermobileapp.R;
import com.damskuy.petfeedermobileapp.data.model.Result;
import com.damskuy.petfeedermobileapp.data.auth.AuthRepository;
import com.damskuy.petfeedermobileapp.data.model.AuthenticatedUser;
import com.damskuy.petfeedermobileapp.utils.ViewUtils;
import com.damskuy.petfeedermobileapp.ui.MainActivity;
import com.damskuy.petfeedermobileapp.ui.register.RegisterActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LoginActivity extends AppCompatActivity {

    private ActivityResultLauncher<Intent> gsActivityResultLauncher;
    private GoogleSignInClient googleSignInClient;
    private TextInputLayout edtLayoutEmail, edtLayoutPassword;
    private EditText edtEmail, edtPassword;
    private AppCompatButton btnLogin;
    private TextView registerLink;
    private LoginViewModel loginViewModel;
    private ImageView googleSignIn, facebookSignIn, twitterSignIn;
    private SweetAlertDialog alertDialog;
    private boolean formValid = false;

    @Override
    protected void onStart() {
        super.onStart();
        if (loginViewModel.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();

        ViewModelProvider loginViewModelProvider =
                new ViewModelProvider(this, new LoginViewModelFactory(getApplication()));

        loginViewModel = loginViewModelProvider.get(LoginViewModel.class);

        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInClient.signOut();

        gsActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    ViewUtils.hideLoadingDialog(alertDialog);
                    Intent data = result.getData();
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        alertDialog = ViewUtils.showLoadingDialog(this);
                        loginViewModel.loginWithGoogle(account);
                    } catch (ApiException e) {
                        ViewUtils.fireErrorAlert(this, "Google sign in failed");
                    }
                }
        );

        observeLoginFormState();
        observeLoginResult();

        initEventHandlers();

        typingInputValidation(edtLayoutEmail, edtEmail);
        typingInputValidation(edtLayoutPassword, edtPassword);
    }

    private void initEventHandlers() {
       registerLinkClickEventHandler();
       loginButtonClickEventHandler();
       gsButtonClickEventHandler();
    }

    private void registerLinkClickEventHandler() {
        registerLink.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void loginButtonClickEventHandler() {
        btnLogin.setOnClickListener(v -> {
            if (formValid) {
                String email = ViewUtils.getEdtText(edtEmail);
                String password = ViewUtils.getEdtText(edtPassword);
                loginViewModel.login(email, password);
                alertDialog = ViewUtils.showLoadingDialog(this);
            } else {
                notifyLoginInputChanged();
                ViewUtils.vibratePhone(
                        getApplicationContext(),
                        (Vibrator) getSystemService(Context.VIBRATOR_SERVICE),
                        findViewById(R.id.login_container),
                        500
                );
            }
        });
    }

    private void gsButtonClickEventHandler() {
        googleSignIn.setOnClickListener(v -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            alertDialog = ViewUtils.showLoadingDialog(this);
            gsActivityResultLauncher.launch(signInIntent);
        });
    }

    private void typingInputValidation(TextInputLayout edtLayout, EditText edt) {
        Handler handler = new Handler();
        Runnable runnable = this::notifyLoginInputChanged;
        TextWatcher typingInputWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                edtLayout.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable s) {
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 700);
            }
        };
        edt.addTextChangedListener(typingInputWatcher);
    }

    private void notifyLoginInputChanged() {
        loginViewModel.loginInputChange(
                ViewUtils.getEdtText(edtEmail),
                ViewUtils.getEdtText(edtPassword)
        );
    }

    private void observeLoginFormState() {
        loginViewModel.getLoginFormState().observe(this, loginFormState -> {
            if (loginFormState.getEmailError() == null) edtLayoutEmail.setErrorEnabled(false);
            else edtLayoutEmail.setError(loginFormState.getEmailError());
            formValid = loginFormState.isFormValid();
        });
    }

    private void observeLoginResult() {
        loginViewModel.getLoginResult().observe(this, loginResult -> {
            ViewUtils.hideLoadingDialog(alertDialog);
            if (loginResult instanceof Result.Success) {
                AuthenticatedUser user = ((Result.Success<AuthenticatedUser>) loginResult).getData();
                AuthRepository.getInstance().createUserSession(LoginActivity.this, user);
                ViewUtils.fireSuccessAlert(this, "Login Success");
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            } else {
                String error = ((Result.Error<AuthenticatedUser>) loginResult).getErrorMessage();
                ViewUtils.fireErrorAlert(this, error);
            }
        });
    }

    private void initUI() {
        edtEmail = findViewById(R.id.edt_email_login);
        edtLayoutEmail = findViewById(R.id.edt_layout_email_login);
        edtPassword = findViewById(R.id.edt_password_login);
        edtLayoutPassword = findViewById(R.id.edt_layout_password_login);
        btnLogin = findViewById(R.id.btn_login);
        registerLink = findViewById(R.id.create);
        googleSignIn = findViewById(R.id.img_google_signin);
        facebookSignIn = findViewById(R.id.img_facebook_signin);
        twitterSignIn = findViewById(R.id.img_twitter_signin);
        textInputConfiguration();
    }

    private void textInputConfiguration() {
        String edtEmailHint = getString(R.string.email_edt_placeholder);
        String edtPasswordHint = getString(R.string.email_edt_placeholder);

        edtEmail.setOnFocusChangeListener((v, hasFocus) ->
                ViewUtils.hideTextInputHint(hasFocus, edtEmail, edtLayoutEmail, edtEmailHint));
        edtPassword.setOnFocusChangeListener((v, hasFocus) ->
                ViewUtils.hideTextInputHint(hasFocus, edtPassword, edtLayoutPassword, edtPasswordHint));
    }
}