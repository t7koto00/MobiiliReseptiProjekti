package com.example.courseclash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawer;
    public TextView navUser;
    public User user = new User();
    public FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_profile:
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                Intent profileIntent = new Intent(getApplicationContext(), Profile.class);
                startActivity(profileIntent);
                break;
            case R.id.nav_favorite:
                Toast.makeText(this, "Favorites are a work in progress.", Toast.LENGTH_SHORT).show();
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavoriteFragment()).commit();
                break;
            case R.id.nav_shopping_cart:
                Intent intent = new Intent(getApplicationContext(), ShoppingList.class);
                startActivity(intent);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShoppingCartFragment()).commit();
                break;
            case R.id.nav_leaderboard:
                Intent leaderBoardIntent = new Intent(getApplicationContext(), leaderboard.class);
                startActivity(leaderBoardIntent);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LeaderboardFragment()).commit();
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Logout succesful", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                Log.d("FSUSER2", "onComplete: " + user.getUserName());

                startActivity(new Intent(BaseActivity.this, Login.class));
                break;
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        final String userID = mAuth.getCurrentUser().getUid();
        navUser = findViewById(R.id.nav_username);

        db.collection("users").document(userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() ){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            user = documentSnapshot.toObject(User.class);
                            Log.d("FSUSER", "onComplete: " + user.getUserName());

                        }
                    }
                });
    }
}
