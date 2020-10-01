package com.prefycs.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    EditText search;
    ImageView img;
    TextView country;
    ListView lst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        search=findViewById(R.id.search);
        lst=findViewById(R.id.lstv);
        country=findViewById(R.id.country);




        img=findViewById(R.id.ico);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadApi();
            }
        });


    }


    public  void LoadApi()
    {



        String query = search.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://coronavirus-19-api.herokuapp.com/countries/" + query;
        country.setText(query);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {


                            String cs = response.getString("cases");
                            String tdc=response.getString("todayCases");
                            String dt = response.getString("deaths");
                            String tdd=response.getString("todayDeaths");
                            String acc=response.getString("active");
                            String rec = response.getString("recovered");

                            String[] array = {"Cases: "+cs,"Today Cases: "+tdc,"Active: " +acc,"Deaths: " +dt,"today deaths: "+tdd,"Recovered: " +rec};
                            ArrayAdapter adapter = new ArrayAdapter<String>(MainActivity.this,R.layout.activity_listview,array);
                            lst.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        Toast.makeText(getApplicationContext(), "Connection Failed",Toast.LENGTH_SHORT).show();


                    }
                });


        queue.add(jsonObjectRequest);


    }
}