package com.example.courseclash;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import io.grpc.Context;

import static android.view.View.GONE;

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
    LinearLayout linearLayout = null;
    ProgressBar progressBar = null;
    TextView textViewUploading = null;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;

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
        linearLayout = findViewById(R.id.linearLayout);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(GONE);
        textViewUploading = findViewById(R.id.textViewUploading);
        textViewUploading.setVisibility(GONE);
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
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "com.example.android.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        } else if (view == galleryButton) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        } else if (view == addButton) {

            if (editTextTitle.getText().length() == 0 || editTextInstructions.getText().length() == 0 || editTextIngredients.getText().length() == 0 || editTextTime.getText().length() == 0) {
                Toast.makeText(AddRecipe.this, "Some of the required fields are empty!", Toast.LENGTH_SHORT).show();
            } else {

                uploadImage();
            }
        }
    }

        void addRecipeToDb()
    {
        recipe.setTags(editTextTags.getText().toString());
        recipe.setTitle(editTextTitle.getText().toString());
        recipe.setTime(editTextTime.getText().toString());
        recipe.setInstructions(editTextInstructions.getText().toString());
        recipe.setIngredients(editTextIngredients.getText().toString());
        //tekijän nimi
        //tekijän id
        //osaksi recipeä!
        ArrayList<Integer> emptyRatingList = new ArrayList<>();
        emptyRatingList.add(0);
        emptyRatingList.add(0);
        emptyRatingList.add(0);
        emptyRatingList.add(0);
        emptyRatingList.add(0);
        emptyRatingList.add(0);
        recipe.setRateAmounts(emptyRatingList);
        recipe.setImage(imageURL);
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
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            File f = new File(currentPhotoPath);
            filePath = Uri.fromFile(f);

            int targetW = imageView.getWidth();
            int targetH = imageView.getHeight();

            // Get the dimensions of the bitmap
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
            int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

            try {
                ExifInterface exif = new ExifInterface(currentPhotoPath);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                Log.d("EXIF", "Exif: " + orientation);
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                }
                else if (orientation == 3) {
                    matrix.postRotate(180);
                }
                else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true); // rotating bitmap
            }
            catch (Exception e) {

            }

            imageView.setImageBitmap(bitmap);
        }
    }


    private void uploadImage() {

        if(filePath != null)
        {
            progressBar.setVisibility(View.VISIBLE);
            textViewUploading.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(LinearLayout.INVISIBLE);

             ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressBar.setVisibility(GONE);
                            textViewUploading.setVisibility(GONE);
                            Toast.makeText(AddRecipe.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            getImageDownloadUrl();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressBar.setVisibility(GONE);
                            textViewUploading.setVisibility(GONE);
                            Toast.makeText(AddRecipe.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            textViewUploading.setText("Uploaded "+(int)progress+"%");
                        }
                    });
        }
        else{
            Toast.makeText(AddRecipe.this, "Choose a picture from your gallery, or take a new one!", Toast.LENGTH_SHORT).show();
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

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
