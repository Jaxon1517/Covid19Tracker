package com.example.covid19tracker;

import android.content.Context;
import android.widget.*;
import android.view.*;
import androidx.annotation.*;
import com.android.volley.*;
import com.android.volley.toolbox.*;

import org.json.*;

public class CountryTrackerAdapter extends ArrayAdapter<String> {

    public CountryTrackerAdapter(Context context, String[] savedCountriesArray) {
        super(context, 0, savedCountriesArray);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        final String currentCountryName = getItem(position);
        TextView savedCountryNameTextView = convertView.findViewById(R.id.Country);
        savedCountryNameTextView.setText(currentCountryName);
        final TextView savedCountryConfirmedTextView = convertView.findViewById(R.id.ConfirmedCases);

        /*Make an API call to get live data for each saved country*/

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="https://api.covid19api.com/summary";

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


                                //check if the current country in the loop is the same as this list item
                                if(currentCountryString.equals(currentCountryName)) {
                                    //set the text of the TextView to be the # of confirmed cases
                                    savedCountryConfirmedTextView.setText(currentCountry.getString("TotalConfirmed"));

                                    //You can set text on other TextViews below here if you have more Views and data you want to show
                                    //e.g. savedCountryRecoveredTextView.setText(currentCountry.getString("TotalRecovered"));

                                }
                            }
                        } catch (Exception e) {
                            savedCountryConfirmedTextView.setText("Country not found");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                savedCountryConfirmedTextView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);


        return convertView;
    }

}
