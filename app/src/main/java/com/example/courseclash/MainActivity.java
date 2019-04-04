package com.example.courseclash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    Button ingredientBtn;
    Button createBtn;
    EditText goToSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, RecipeListView.class);
        startActivity(intent);


    }
}
