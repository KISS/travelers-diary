package com.example.travelapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travelapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;
    EditText emailEditTextView, signUpPasswordEditTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();
        emailEditTextView = (EditText) findViewById(R.id.userName);
        signUpPasswordEditTextView = (EditText) findViewById(R.id.passWord);

        findViewById(R.id.signupPage).setOnClickListener(this);
        findViewById(R.id.signinButton).setOnClickListener(this);
    }

    private void userLogin() {

        String emailaddress = emailEditTextView.getText().toString().trim();
        String Signuppassword = signUpPasswordEditTextView.getText().toString().trim();

        if (emailaddress.isEmpty()) {
            emailEditTextView.setError("E-mail is required");
            emailEditTextView.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailaddress).matches()) {
            emailEditTextView.setError("Enter a valid E-mail address");
            emailEditTextView.requestFocus();
            return;
        }

        if (Signuppassword.isEmpty()) {
            signUpPasswordEditTextView.setError("Password is required");
            signUpPasswordEditTextView.requestFocus();
            return;
        }

        if (Signuppassword.length() < 6) {
            signUpPasswordEditTextView.setError("Minimum length of password should be 6");
            signUpPasswordEditTextView.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(emailaddress, Signuppassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    if (mAuth.getCurrentUser().isEmailVerified()) {

                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.verify_email_password), Toast.LENGTH_SHORT).show();
                    emailEditTextView.setText("");
                    signUpPasswordEditTextView.setText("");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signinButton:
                userLogin();
                break;
            case R.id.signupPage:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
        }
    }
}