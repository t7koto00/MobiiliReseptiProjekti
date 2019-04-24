package com.example.courseclash;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private EditText emailField;
    private EditText passwordField;

    private Button loginButton, singupButton;


    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mAuth = FirebaseAuth.getInstance();

        emailField = findViewById(R.id.emailView);
        passwordField = findViewById(R.id.passwordView);

        loginButton = findViewById(R.id.loginButton);
        singupButton = findViewById(R.id.signUpButton);

        singupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register_user.class));


            }
        });




            mAuthListener = new FirebaseAuth.AuthStateListener(){
                @Override
                public  void  onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                    FirebaseUser fUser = FirebaseAuth.getInstance().getCurrentUser();
                    if(fUser!=null){

                        Intent intent = new Intent(Login.this, RecipeListView.class);
                        startActivity(intent);

                        finish();
                    }
                }


            };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eMail = emailField.getText().toString();
                String passWord = passwordField.getText().toString();
                Log.d("Credentials", "pass: "+ passWord + " email: " + eMail);
                startSingIn(eMail,passWord);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

      mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }




    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void startSingIn(String eMail, String passWord) {



        if(TextUtils.isEmpty(eMail) || TextUtils.isEmpty(passWord)){

            Toast.makeText(this, "Fields are empty", Toast.LENGTH_LONG).show();

        } else {

            mAuth.signInWithEmailAndPassword(eMail, passWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        startActivity(new Intent(Login.this, RecipeListView.class));




                    } else {
                        Toast.makeText(Login.this, "Sign In Problem", Toast.LENGTH_LONG).show();
                    }

                }
            });
        }


    }
}
