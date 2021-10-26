package com.moringaschool.myrestaurants.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.moringaschool.myrestaurants.Constants;
import com.moringaschool.myrestaurants.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private ValueEventListener mSearchedLocationReferenceListener;
    private DatabaseReference mSearchedLocationReference;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    // public static final String TAG = MainActivity.class.getSimpleName();
//   private Button mFindRestaurantsButton;
//    private EditText mLocationEditText;
//    private TextView mAppNameTextView;

    @BindView(R.id.findRestaurantsButton) Button mFindRestaurantsButton;
  //  @BindView(R.id.locationEditText) EditText mLocationEditText;
    @BindView(R.id.appNameTextView) TextView mAppNameTextView;
    @BindView(R.id.savedRestaurantsButton) Button mSavedRestaurantsButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        mSearchedLocationReference = FirebaseDatabase
//                .getInstance()
//                .getReference()
//                .child(Constants.FIREBASE_CHILD_SEARCHED_LOCATION);


       // mSearchedLocationReference.addValueEventListener(new ValueEventListener() { //attach listener

//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) { //something changed!
//                for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
//                    String location = locationSnapshot.getValue().toString();
//                    Log.d("Locations updated", "location: " + location); //log
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) { //update UI here if error occurred.
//
//            }
//        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mFindRestaurantsButton.setOnClickListener(this);
        mSavedRestaurantsButton.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    getSupportActionBar().setTitle("Welcome, " + user.getDisplayName() + "!");
                } else {


                }
            }


        };
//        mLocationEditText = (EditText) findViewById(R.id.locationEditText);
//        mFindRestaurantsButton = (Button)findViewById(R.id.findRestaurantsButton);

    }



    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
            @Override
            public void onClick(View v) {
                if(v == mFindRestaurantsButton) {
                   // String location = mLocationEditText.getText().toString();
                  //  saveLocationToFirebase(location);

                    //Log.d(TAG, location);
                    Intent intent = new Intent(MainActivity.this, RestaurantsActivity.class);
                   // intent.putExtra("location", location);
                    startActivity(intent);
                    //Toast.makeText(MainActivity.this, "Hello World!", Toast.LENGTH_LONG).show();
               }
                if (v == mSavedRestaurantsButton) {
                    Intent intent = new Intent(MainActivity.this, SavedRestaurantActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_logout) {
                    logout();
                    return true;
                }
                return super.onOptionsItemSelected(item);
            }

            //menu bar
            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_main, menu);
                return super.onCreateOptionsMenu(menu);
            }


            private void logout() {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }






//    public void saveLocationToFirebase(String location) {
//        mSearchedLocationReference.push().setValue(location);


//       //earlier: mSearchedLocationReference.setValue(location);
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mSearchedLocationReference.removeEventListener(mSearchedLocationReferenceListener);
//    }

}
