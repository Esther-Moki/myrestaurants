package com.moringaschool.myrestaurants.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.moringaschool.myrestaurants.Constants;
import com.moringaschool.myrestaurants.R;
import com.moringaschool.myrestaurants.adapters.FirebaseRestaurantViewHolder;
import com.moringaschool.myrestaurants.models.Business;



import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedRestaurantActivity extends AppCompatActivity{

    private DatabaseReference mRestaurantReference;
    private FirebaseRecyclerAdapter<Business, FirebaseRestaurantViewHolder> mFirebaseAdapter;

    // private  FirebaseRestaurantListAdapter mFirebaseAdapter;
   // private  ItemTouchHelper mItemTouchHelper;


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.errorTextView)
    TextView mErrorTextView;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants);
        ButterKnife.bind(this);


          //grabbing the currently authorized user's UID in onCreate()
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        mRestaurantReference = FirebaseDatabase
                .getInstance()
                .getReference(Constants.FIREBASE_CHILD_RESTAURANTS).child(uid);

        setUpFirebaseAdapter();
        hideProgressBar();
        showRestaurants();


    }


    private void setUpFirebaseAdapter(){
        FirebaseRecyclerOptions<Business> options =
                new FirebaseRecyclerOptions.Builder<Business>()
                        .setQuery(mRestaurantReference, Business.class)
                        .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Business, FirebaseRestaurantViewHolder>(options) {
            @Override
            protected void onBindViewHolder(
                    @NonNull FirebaseRestaurantViewHolder firebaseRestaurantViewHolder, int position, @NonNull Business restaurant) {
                firebaseRestaurantViewHolder.bindRestaurant(restaurant);
            }

            @NonNull
            @Override
            public FirebaseRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_list_item_drag, parent, false);
                return new FirebaseRestaurantViewHolder(view);
            }
        };

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mFirebaseAdapter!= null) {
            mFirebaseAdapter.stopListening();
        }
    }
//    @Override
//    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
//        mItemTouchHelper.startDrag(viewHolder);
//    }

    private void showRestaurants() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }


}





//   private void setUpFirebaseAdapter() {
//       FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//       String uid = user.getUid();
//       mRestaurantReference = FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_RESTAURANTS).child(uid);
//       FirebaseRecyclerOptions<Business> options =
//               new FirebaseRecyclerOptions.Builder<Business>()
//                       .setQuery(mRestaurantReference, Business.class)
//                       .build();
//
//       mFirebaseAdapter = new FirebaseRestaurantListAdapter(options, mRestaurantReference, (OnStartDragListener) this, this);
//       mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//       mRecyclerView.setAdapter(mFirebaseAdapter);
//       mRecyclerView.setHasFixedSize(true);
//
//       ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(mFirebaseAdapter);
//       mItemTouchHelper = new ItemTouchHelper(callback);
//       mItemTouchHelper.attachToRecyclerView(mRecyclerView);
//       mRecyclerView.setAdapter();
//
//   }
