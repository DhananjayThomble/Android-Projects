package com.prefycs.covidtracker;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    Spinner search;
    ImageView img;
    TextView country;
    ListView lst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search = findViewById(R.id.search);
        lst = findViewById(R.id.lstv);
        country = findViewById(R.id.country);

        //This will populate country name.
        popCountry();


        img = findViewById(R.id.ico);
        search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LoadApi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadApi();
            }
        });


    }

    public void popCountry() {
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }

        Collections.sort(countries);
//

        ArrayAdapter<String> countryAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_spinner_item, countries);

        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the your spinner
        search.setAdapter(countryAdapter);
    }

    public void LoadApi() {


        String query = search.getSelectedItem().toString();

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://coronavirus-19-api.herokuapp.com/countries/" + query;
        country.setText(query);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            String cs = response.getString("cases");
                            String tdc = response.getString("todayCases");
                            String dt = response.getString("deaths");
                            String tdd = response.getString("todayDeaths");
                            String acc = response.getString("active");
                            String rec = response.getString("recovered");

                            String[] array = {"Cases: " + cs, "Today Cases: " + tdc, "Active: " + acc, "Deaths: " + dt, "today deaths: " + tdd, "Recovered: " + rec};
                            ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.activity_listview, array);
                            lst.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(getApplicationContext(), "Connection Failed", Toast.LENGTH_SHORT).show();


                    }
                });


        queue.add(jsonObjectRequest);


    }
}
