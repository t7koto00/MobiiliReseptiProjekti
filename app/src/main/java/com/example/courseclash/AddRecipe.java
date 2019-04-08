package com.example.courseclash;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

import io.grpc.Context;

public class AddRecipe extends AppCompatActivity implements View.OnClickListener {

    public EditText editTextTitle = null;
    public EditText editTextTime = null;
    public EditText editTextTags = null;
    public EditText editTextInstructions = null;
    public EditText editTextIngredients = null;
    public Button galleryButton = null;
    public Button cameraButton = null;
    public Button addButton = null;
    Recipe recipe = new Recipe();
    FirebaseFirestore db = null;
    public ImageView imageView = null;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference ref = null;
    String imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        editTextIngredients = findViewById(R.id.editTextIngredients);
        editTextInstructions= findViewById(R.id.editTextInstructions);
        editTextTags = findViewById(R.id.editTextTags);
        editTextTime = findViewById(R.id.editTextTime);
        editTextTitle = findViewById(R.id.editTextTitle);
        galleryButton = findViewById(R.id.galleryButton);
        cameraButton = findViewById(R.id.cameraButton);
        addButton = findViewById(R.id.addButton);
        galleryButton.setOnClickListener(this);
        cameraButton.setOnClickListener(this);
        addButton.setOnClickListener(this);
        imageView = findViewById(R.id.imageViewAdd);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


    }

    @Override
    public void onClick(View view) {
        if (view == cameraButton) {
        //kamera kuvan ottoa varten jos mahdollista


        }
        else if (view == galleryButton){
        // kuvan valinta galleriasta
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }
        else if (view == addButton){

            uploadImage();

        }
        }

        void addRecipeToDb()
    {
        recipe.setTags(editTextTags.getText().toString());
        recipe.setTitle(editTextTitle.getText().toString());
        recipe.setTime(editTextTime.getText().toString());
        recipe.setInstructions(editTextInstructions.getText().toString());
        recipe.setIngredients(editTextIngredients.getText().toString());
        recipe.setImage(imageURL);
        //kuva!!
        db.collection("recipes").add(recipe).
                addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
        @Override
        public void onSuccess(DocumentReference documentReference) {
            Log.d("TAG", "DocumentSnapshot written with ID: " + documentReference.getId());
            recipe.setId(documentReference.getId());
            String id = recipe.getId();
            db.collection("recipes").document(recipe.getId()).set(recipe);

            Intent intent = new Intent(getApplicationContext(), RecipeView.class);
            intent.putExtra("DATA", id);
            startActivity(intent);
            finish();
        }
    });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

             ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(AddRecipe.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            getImageDownloadUrl();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddRecipe.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    private void getImageDownloadUrl(){
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                recipe.setImage(uri.toString());
                imageURL = uri.toString();
                Log.d("TAG", uri.toString());
                addRecipeToDb();
                // Got the download URL for 'users/me/profile.png'
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }
}
