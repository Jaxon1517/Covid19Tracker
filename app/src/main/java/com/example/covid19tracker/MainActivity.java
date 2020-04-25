package com.example.covid19tracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView confirmedTextView;
    EditText searchBar;
    TextView extraText;

    ListView lv;
    ArrayList<String> cityList;
    ArrayAdapter<String> aa;
    DatabaseReference databaseMessages;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        confirmedTextView = findViewById(R.id.confirmed_tv);
        searchBar = findViewById(R.id.search_bar);
        extraText = findViewById(R.id.extra);
        FirebaseDatabase database = FirebaseDatabase.getInstance();



    }
    public void fetchData(View view) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.covid19api.com/summary";
        String searchText = searchBar.getText().toString().toLowerCase();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject responseObject = new JSONObject(response);
                            JSONArray countriesArray = responseObject.getJSONArray("Countries");
                            for(int i = 0; i < countriesArray.length(); i++) {
                                JSONObject currentCountry = countriesArray.getJSONObject(i);
                                String currentCountryString = currentCountry.getString("Country").toLowerCase();
                                String countrySearched = searchBar.getText().toString().toLowerCase();
                                if(currentCountryString.equals(countrySearched)) {
                                    confirmedTextView.setText(currentCountry.getString("TotalConfirmed"));
                                }

                            }
                            extraText.setText("Confirmed cases ");
                        } catch (JSONException e) {
                            confirmedTextView.setText("Country name not found");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                confirmedTextView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void addTrack(View view){
        SharedPreferences sharedPrefs = getSharedPreferences("MySharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        String Track = sharedPrefs.getString("Track", "");

        extraText.setText(searchBar.getText().toString() + " Has been Added.");

    }

    public void viewTracker(View view){
        Intent intentToOpenTracked = new Intent(this, TrackedCountries.class);
        startActivity(intentToOpenTracked);
    }
}
