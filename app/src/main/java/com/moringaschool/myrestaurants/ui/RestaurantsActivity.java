package com.moringaschool.myrestaurants.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


import com.moringaschool.myrestaurants.Constants;
import com.moringaschool.myrestaurants.MyRestaurantsArrayAdapter;
import com.moringaschool.myrestaurants.R;
import com.moringaschool.myrestaurants.adapters.RestaurantListAdapter;
import com.moringaschool.myrestaurants.models.Business;
import com.moringaschool.myrestaurants.models.Category;
import com.moringaschool.myrestaurants.models.YelpBusinessesSearchResponse;
import com.moringaschool.myrestaurants.network.YelpApi;
import com.moringaschool.myrestaurants.network.YelpClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantsActivity extends AppCompatActivity {
//    private TextView mLocationTextView;
//    private ListView mListView;
        private static final String TAG = RestaurantsActivity.class.getSimpleName();
        private RestaurantListAdapter mAdapter;
        public List<Business> restaurants;

        private SharedPreferences mSharedPreferences;
        private SharedPreferences.Editor mEditor;
        private String mRecentAddress;

   // @BindView(R.id.locationTextView) TextView mLocationTextView;
   // @BindView(R.id.listView) ListView mListView;
       @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
        @BindView(R.id.errorTextView) TextView mErrorTextView;
        @BindView(R.id.progressBar) ProgressBar mProgressBar;


//    private String[] restaurants = new String[] {"Mi Mero Mole", "Mother's Bistro",
//            "Life of Pie", "Screen Door", "Luc Lac", "Sweet Basil",
//            "Slappy Cakes", "Equinox", "Miss Delta's", "Andina",
//            "Lardo", "Portland City Grill", "Fat Head's Brewery",
//            "Chipotle", "Subway"};
//    private String[] cuisines = new String[] {"Vegan Food", "Breakfast", "Fishs Dishs",
//            "Scandinavian", "Coffee", "English Food", "Burgers", "Fast Food", "Noodle Soups", "Mexican", "BBQ", "Cuban", "Bar Food", "Sports Bar", "Breakfast", "Mexican" };

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_restaurants);
            ButterKnife.bind(this);

            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            mRecentAddress = mSharedPreferences.getString(Constants.PREFERENCES_LOCATION_KEY, null);
            if(mRecentAddress != null){
                fetchRestaurants(mRecentAddress);
            }
        }

//        mListView = (ListView) findViewById(R.id.listView);
//        mLocationTextView = (TextView) findViewById(R.id.locationTextView);


      //  ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, restaurants);

       // MyRestaurantsArrayAdapter adapter = new MyRestaurantsArrayAdapter(this, android.R.layout.simple_list_item_1, restaurants, cuisines); // the arguments must match constructor's parameters!
        //mListView.setAdapter(adapter);


//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                String restaurant = ((TextView)view).getText().toString();
//                Toast.makeText(RestaurantsActivity.this, restaurant, Toast.LENGTH_LONG).show();
//            }
//        });
        //Intent intent = getIntent();
        //String location = intent.getStringExtra("location");
       //mLocationTextView.setText("Here are all the restaurants near: " + location);
            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.menu_search, menu);
                ButterKnife.bind(this);

                mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                mEditor = mSharedPreferences.edit();

                MenuItem menuItem = menu.findItem(R.id.action_search);
                SearchView searchView = (SearchView) menuItem.getActionView();

                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String location) {
                        addToSharedPreferences(location);
                        fetchRestaurants(location);
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String location) {
                        return false;
                    }
                });

                return true;
            }
        @Override
        public boolean onOptionsItemSelected(MenuItem item){
            return super.onOptionsItemSelected(item);
        }

        private void showFailureMessage() {
            mErrorTextView.setText("Something went wrong. Please check your Internet connection and try again later");
            mErrorTextView.setVisibility(View.VISIBLE);
        }

        private void showUnsuccessfulMessage() {
            mErrorTextView.setText("Something went wrong. Please try again later");
            mErrorTextView.setVisibility(View.VISIBLE);
        }

        private void showRestaurants() {
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        private void hideProgressBar() {
            mProgressBar.setVisibility(View.GONE);
        }

        private void addToSharedPreferences(String location) {
            mEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();
        }

        private void fetchRestaurants(String location){
            YelpApi client = YelpClient.getClient();
            Call<YelpBusinessesSearchResponse> call = client.getRestaurants(location, "restaurants");
            call.enqueue(new Callback<YelpBusinessesSearchResponse>() {
                @Override
                public void onResponse(Call<YelpBusinessesSearchResponse> call, Response<YelpBusinessesSearchResponse> response) {

                    hideProgressBar();

                    if (response.isSuccessful()) {
                        restaurants = response.body().getBusinesses();
                        mAdapter = new RestaurantListAdapter(RestaurantsActivity.this, restaurants);
                        mRecyclerView.setAdapter(mAdapter);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RestaurantsActivity.this);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setHasFixedSize(true);

                        showRestaurants();
                    } else {
                        showUnsuccessfulMessage();
                    }
                }

                @Override
                public void onFailure(Call<YelpBusinessesSearchResponse> call, Throwable t) {
                    Log.e(TAG, "onFailure: ",t );
                    hideProgressBar();
                    showFailureMessage();
                }

            });
        }
}



















//        YelpApi client = YelpClient.getClient();
//
//        Call<YelpBusinessesSearchResponse> call = client.getRestaurants(location, "restaurants");
//
//            call.enqueue(new Callback<YelpBusinessesSearchResponse>() {
//            @Override
//            public void onResponse(Call<YelpBusinessesSearchResponse> call, Response<YelpBusinessesSearchResponse> response) {
//                hideProgressBar();
//
//                if (response.isSuccessful()) {
//                    restaurants = response.body().getBusinesses();
//                    mAdapter = new RestaurantListAdapter(RestaurantsActivity.this, restaurants);
//                    mRecyclerView.setAdapter(mAdapter);
//                    RecyclerView.LayoutManager layoutManager =
//                            new LinearLayoutManager(RestaurantsActivity.this);
//                    mRecyclerView.setLayoutManager(layoutManager);
//                    mRecyclerView.setHasFixedSize(true);
//
//    //                List<Business> restaurantsList = response.body().getBusinesses();
//    //                String[] restaurants = new String[restaurantsList.size()];
//    //                String[] categories = new String[restaurantsList.size()];
//    //
//    //                for (int i = 0; i < restaurants.length; i++){
//    //                    restaurants[i] = restaurantsList.get(i).getName();
//    //                }
//    //
//    //                for (int i = 0; i < categories.length; i++) {
//    //                    Category category = restaurantsList.get(i).getCategories().get(0);
//    //                    categories[i] = category.getTitle();
//    //                }
//    //
//    //                ArrayAdapter adapter
//    //                        = new MyRestaurantsArrayAdapter(RestaurantsActivity.this, android.R.layout.simple_list_item_1, restaurants, categories);
//    //                mListView.setAdapter(adapter);
//
//                    showRestaurants();
//                } else {
//                    showUnsuccessfulMessage();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<YelpBusinessesSearchResponse> call, Throwable t) {
//                Log.e(TAG, "onFailure: ",t );
//                hideProgressBar();
//                showFailureMessage();
//            }
//
//        });
//            return false;
//
//        private void addToSharedPreferences(String location) {
//            mEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();
//        }
//   }
////    @Override
////    public boolean onCreateOptionsMenu(Menu menu) {
////        MenuInflater inflater = getMenuInflater();
////        inflater.inflate(R.menu.menu_search, menu);
////        ButterKnife.bind(this);
////
////        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
////        mEditor = mSharedPreferences.edit();
////
////        MenuItem menuItem = menu.findItem(R.id.action_search);
////        SearchView searchView = (SearchView) menuItem.getActionView();
////
////        return true;
////    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void showFailureMessage() {
//        mErrorTextView.setText("Something went wrong. Please check your Internet connection and try again later");
//        mErrorTextView.setVisibility(View.VISIBLE);
//    }
//
//    private void showUnsuccessfulMessage() {
//        mErrorTextView.setText("Something went wrong. Please try again later");
//        mErrorTextView.setVisibility(View.VISIBLE);
//    }
//
//    private void showRestaurants() {
//        mRecyclerView.setVisibility(View.VISIBLE);
////        mListView.setVisibility(View.VISIBLE);
////        mLocationTextView.setVisibility(View.VISIBLE);
//    }
//
//    private void hideProgressBar() {
//        mProgressBar.setVisibility(View.GONE);
//    }
//
//    private void addToSharedPreferences(String location) {
//                    mEditor.putString(Constants.PREFERENCES_LOCATION_KEY, location).apply();
//                }
//
//}