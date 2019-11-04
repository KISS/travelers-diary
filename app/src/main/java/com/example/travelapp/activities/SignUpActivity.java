package com.example.travelapp.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travelapp.R;
import com.example.travelapp.configs.Constants;
import com.example.travelapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth mAuth;

    EditText Email, signupPassword, firstNameEditTextView, lastNameEditTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FirebaseApp.initializeApp(this);

        Email = (EditText) findViewById(R.id.email);
        signupPassword = (EditText) findViewById(R.id.signupPassword);
        firstNameEditTextView = (EditText) findViewById(R.id.firstName);
        lastNameEditTextView = (EditText) findViewById(R.id.lastName);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signinBtn).setOnClickListener(this);
        findViewById(R.id.signupBtn).setOnClickListener(this);


    }

    private void RegisterNewUser() {
        String emailaddress = Email.getText().toString().trim();
        String Signuppassword = signupPassword.getText().toString().trim();

        final String firstName = firstNameEditTextView.getText().toString().trim();
        final String lastName = lastNameEditTextView.getText().toString().trim();

        if (firstName.isEmpty()) {
            firstNameEditTextView.setError("First Name is required");
            firstNameEditTextView.requestFocus();
            return;
        }

        if (lastName.isEmpty()) {
            lastNameEditTextView.setError("Last Name is required");
            lastNameEditTextView.requestFocus();
            return;
        }

        if (emailaddress.isEmpty()) {
            Email.setError("E-mail is required");
            Email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailaddress).matches()) {
            Email.setError("Enter a valid E-mail address");
            Email.requestFocus();
            return;
        }

        if (Signuppassword.isEmpty()) {
            signupPassword.setError("Password is required");
            signupPassword.requestFocus();
            return;
        }

        if (Signuppassword.length() < 6) {
            signupPassword.setError("Minimum length of password should be 6");
            signupPassword.requestFocus();
            return;
        }


        mAuth.createUserWithEmailAndPassword(emailaddress, Signuppassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser user = mAuth.getCurrentUser();

                    String email = user.getEmail();
                    String uid = user.getUid();

//                    HashMap<Object, String> hashMap = new HashMap<>();
//
//                    hashMap.put("email", email);
//                    hashMap.put("uid", uid);
//                    hashMap.put("firstname", firstName);
//                    hashMap.put("lastname", lastName);
//                    hashMap.put("gradyear", ""); // need revision
//                    hashMap.put("image", "");

                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference(Constants.DATABASE_PATH_USERS);

                    User userObj = new User(email, firstName, lastName, "", 0L, uid);
                    reference.child(uid).setValue(userObj);

                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), getString(R.string.register_successful), Toast.LENGTH_SHORT).show();
                                Email.setText("");
                                signupPassword.setText("");
                                finish();
                                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
                            }
                        }
                    });
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), getString(R.string.already_registered), Toast.LENGTH_SHORT).show();
                        Email.setText("");
                        signupPassword.setText("");
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signupBtn:
                RegisterNewUser();
                break;
            case R.id.signinBtn:
                finish();
                startActivity(new Intent(this, SignInActivity.class));
                break;
        }
    }


    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // get which view was focused
            View v = getCurrentFocus();

            if (HideKeyboard.getInstance().isTouchOutsideView(v, ev)) {
                HideKeyboard.getInstance().hideSoftInput(v.getWindowToken(), this);
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}