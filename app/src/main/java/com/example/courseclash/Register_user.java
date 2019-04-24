package com.example.courseclash;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register_user extends AppCompatActivity {

    private EditText editUserName, editEmail, editPassword, editPassword2;
    private Button rgstrButton;
    private ProgressBar progressBar2;
    private FirebaseAuth mAuth;
    FirebaseFirestore db2 = null;
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        editUserName = findViewById(R.id.newUsernameField);
        editEmail = findViewById(R.id.newEmailField);
        editPassword = findViewById(R.id.newPassField);
        editPassword2 = findViewById(R.id.newPassField2);

        progressBar2 = findViewById(R.id.progressBar2);
        progressBar2.setVisibility(View.GONE);

        rgstrButton = findViewById(R.id.registerButton);

        db2 = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();

        rgstrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.registerButton:
                        RegisterUser();
                        break;
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null) {

            startActivity(new Intent(Register_user.this, RecipeListView.class));
            overridePendingTransition(R.anim.slide_out_down, R.anim.slide_in_up);

        }
    }

    private void RegisterUser(){
        final String username = editUserName.getText().toString().trim();
        final String eMail = editEmail.getText().toString().trim();
        String pass = editPassword.getText().toString().trim();
        final String pass2 = editPassword2.getText().toString().trim();

        if(username.isEmpty()) {
            editUserName.setText("Username Required");
            editUserName.requestFocus();
            return;
        }
        if(eMail.isEmpty()){
            editEmail.setText("E-mail Required");
            editEmail.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            editPassword.setText("Password Required");
            editPassword.requestFocus();
            return;
        }
        if(pass2.isEmpty()){
            editPassword2.setHint("Please Re-type Password");
            Toast.makeText(this, "Please Re-type Password", Toast.LENGTH_LONG).show();
            editPassword2.requestFocus();
            return;
        }
        if(!pass2.equals(pass)){
            editPassword2.setHint("Password does not match");
            Toast.makeText(this, "Password does not match", Toast.LENGTH_LONG).show();
            editPassword2.requestFocus();
            return;
        }


        progressBar2.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(eMail,pass2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){


                    user.setUserName(username);
                    user.setEmail(eMail);

                    /*db2.collection("users").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
*/
                            user.setUserID(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            db2.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(user);
                            progressBar2.setVisibility(View.GONE);
                            finish();

                   // });
                } else {
                    Toast.makeText(Register_user.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar2.setVisibility(View.GONE);


                }

            }
        });
    }
}
