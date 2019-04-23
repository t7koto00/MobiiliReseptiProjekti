package com.example.courseclash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

public class ShoppingList extends AppCompatActivity implements View.OnClickListener {

    public FloatingActionButton fabAdd = null;
    public FloatingActionButton fabClear = null;
    public EditText editTextAdd = null;
    public ListView listView = null;
    ArrayList<String> shoppingList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        editTextAdd = findViewById(R.id.editTextAdd);
        listView = findViewById(R.id.shoppingList);
        fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(this);
        fabClear = findViewById(R.id.fabClear);
        fabClear.setOnClickListener(this);

        shoppingList = read(this);
        if (shoppingList == null) {
            shoppingList = new ArrayList<>();
        }
        else{
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, shoppingList);
            listView.setAdapter(arrayAdapter);
        }
    }

    @Override
    public void onClick(View view) {

        if(view == fabAdd){
            shoppingList.add(editTextAdd.getText().toString());
            editTextAdd.setText("");
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, shoppingList);
            listView.setAdapter(arrayAdapter);
            write(this, shoppingList);
            shoppingList = read(this);
        }
        if (view == fabClear){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to clear your shoppinglist?")
                    .setPositiveButton("Clear", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ArrayList<String> emptyList = new ArrayList<>();
                            shoppingList = emptyList;
                            write(getApplicationContext(), shoppingList);
                            shoppingList = read(getApplicationContext());
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, shoppingList);
                            listView.setAdapter(arrayAdapter);
                        }
                    })
                    .setNegativeButton("Dont clear", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
        });
            AlertDialog alert = builder.create();
            alert.show();
    }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        write(this,shoppingList);

    }

    public static void write(Context context, Object nameOfClass) {
        File directory = new File(context.getFilesDir().getAbsolutePath()
                + File.separator + "serlization");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filename = "shoppingList.srl";
        ObjectOutput out = null;

        try {
            out = new ObjectOutputStream(new FileOutputStream(directory
                    + File.separator + filename));
            out.writeObject(nameOfClass);
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> read(Context context) {

        ObjectInputStream input = null;
        ArrayList<String> ReturnClass = null;
        String filename = "shoppingList.srl";
        File directory = new File(context.getFilesDir().getAbsolutePath()
                + File.separator + "serlization");
        try {

            input = new ObjectInputStream(new FileInputStream(directory
                    + File.separator + filename));
            ReturnClass = (ArrayList<String>) input.readObject();
            input.close();

        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ReturnClass;
    }

}
