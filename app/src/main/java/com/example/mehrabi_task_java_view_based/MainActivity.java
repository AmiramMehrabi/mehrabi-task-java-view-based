package com.example.mehrabi_task_java_view_based;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mehrabi_task_java_view_based.db.AppDatabase;
import com.example.mehrabi_task_java_view_based.db.User;
import com.google.android.material.textfield.TextInputLayout;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout email, fullName, password;
    private Button registerButt;

    private AppDatabase appDatabase;
    private ExecutorService databaseExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        appDatabase = AppDatabase.getInstance(getApplicationContext());
        databaseExecutor = Executors.newSingleThreadExecutor();

        email = findViewById(R.id.email_text_field);
        fullName = findViewById(R.id.fullname_text_field);
        password = findViewById(R.id.password_text_field);
        registerButt = findViewById(R.id.register_button);

        registerButt.setOnClickListener(v -> {
            validateAndProcessInput();
        });

        clearErrorOnTextChanged(email);
        clearErrorOnTextChanged(fullName);
        clearErrorOnTextChanged(password);
    }

    private void validateAndProcessInput() {

        String emailTxt = email.getEditText().getText().toString().trim();
        String fullNameTxt = fullName.getEditText().getText().toString().trim();
        String passwordTxt = password.getEditText().getText().toString().trim();

        boolean isFullNameValid = validateFullName(fullNameTxt);
        boolean isEmailValid = validateEmail(emailTxt);
        boolean isPasswordValid = validatePassword(passwordTxt);

        if (isFullNameValid && isEmailValid && isPasswordValid) {
            String textToShow = "ثبت نام موفقیت آمیز بود.";

            Toast.makeText(
                    this,
                    textToShow,
                    LENGTH_LONG
            ).show();

            long registrationTime = System.currentTimeMillis();
            User newUser = new User(fullNameTxt, emailTxt, passwordTxt, registrationTime);

            databaseExecutor.execute(() -> {
                appDatabase.userDao().insertUser(newUser);

                runOnUiThread(() -> {
                    Toast.makeText(this, "کاربر با موفقیت ثبت شد!", Toast.LENGTH_SHORT).show();
                });
            });

        }
    }

    private boolean validatePassword(String passwordTxt) {
        if (TextUtils.isEmpty(passwordTxt)) {
            password.setError("رمز عبور نمی‌تواند خالی باشد.");
            return false;
        } else if (passwordTxt.length() < 6) {
            password.setError("رمز عبور باید حداقل ۶ کاراکتر باشد.");
            return false;
        } else {
            password.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail(String emailTxt) {
        if (TextUtils.isEmpty(emailTxt)) {
            email.setError("ایمیل نمی‌تواند خالی باشد.");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailTxt).matches()) {
            email.setError("فرمت ایمیل نامعتبر است.");
            return false;
        } else {
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateFullName(String fullNameTxt) {
        if (TextUtils.isEmpty(fullNameTxt)) {
            fullName.setError("نام و نام خانوادگی نمیتواند خالی باشد.");
            return false;
        } else {
            fullName.setErrorEnabled(false);
            return true;
        }
    }

    private void clearErrorOnTextChanged(TextInputLayout input) {
        if (input.getEditText() != null) {
            input.getEditText().addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (input.isErrorEnabled()) {
                        input.setErrorEnabled(false);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }
}