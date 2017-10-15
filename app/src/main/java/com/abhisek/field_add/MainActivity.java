package com.abhisek.field_add;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;

public class MainActivity extends AppCompatActivity {

    ArrayList<Details> farmType;
    ArrayAdapter<Details> farmTypeAdapter;
    Spinner farmTypeSpinner;
    String farmTypeId;
    private Context mContext;
    TextView count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext =this;

        farmType = new ArrayList<>();
        farmTypeSpinner = (Spinner)findViewById(R.id.farm_selected_value);
        farmTypeSpinner.setPrompt("Headline");
        new getFarmType().execute("https://api.nytimes.com/svc/search/v2/articlesearch.json");
        count = (TextView) findViewById(R.id.countTextView);



        farmTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.i("Method Called", "onItemSelected");


                Details farmType = farmTypeAdapter.getItem(i);
                farmTypeId= farmType.getId();
                Log.i("styleId", farmTypeId);
                count.setText(farmTypeId);
            //    Toast.makeText(mContext, farmTypeId, Toast.LENGTH_LONG);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Log.i("Method Called", "onNothingSelected");

            }
        });



    }

    public class getFarmType extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            String apikey = "028c8858b1094d22999249803babe4fa";
            try {
                URL url = new URL(params[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
               urlConnection.setRequestProperty("api-key", apikey);

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {

                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                } finally {

                    String status = urlConnection.getResponseMessage();
                    Log.i("status", String.valueOf(status));

                    Log.i("urlConnect", String.valueOf(urlConnection.getErrorStream()));
                    urlConnection.disconnect();

                }

            } catch (java.io.IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String response) {


            if (response == null) {
                response = "THERE WAS AN ERROR";
            }

            else {

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONObject innerResponse= jsonObject.getJSONObject("response");

                    JSONArray docs = innerResponse.getJSONArray("docs");

                    for(int i=0;i<docs.length();i++) {
                        JSONObject details = docs.getJSONObject(i);
                        JSONObject headline = details.getJSONObject("headline");

                        String main = headline.getString("main");
                        String count = details.getString("word_count");
                        farmType.add(new Details(main,count));
                    }


                    farmTypeAdapter = new ArrayAdapter<Details>(mContext, simple_list_item_1, farmType);
                    farmTypeSpinner.setAdapter(farmTypeAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            Log.i("response", response);
        }

    }
}
