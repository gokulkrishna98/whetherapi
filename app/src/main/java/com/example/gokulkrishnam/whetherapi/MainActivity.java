package com.example.gokulkrishnam.whetherapi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    GoogleApiClient mGoogleApiClient;
    TextView textView;
    TextView textview2;
    ImageView imageview;
    Button searchmore;
    String iconid;
    String imageurl;
    String name="Whether app";
    String cityid;
    ProgressDialog dialog;
    Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView= (TextView) findViewById(R.id.textView);
        textview2=(TextView)findViewById(R.id.textView2);
        imageview=(ImageView)findViewById(R.id.imageView);
        searchmore=(Button)findViewById(R.id.button2);
        imageview.setImageResource(R.drawable.download);

        searchmore.setVisibility(View.INVISIBLE);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("vivz", "Place: " + place.getName());
                name=place.getName().toString();
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("vivz", "An error occurred: " + status);
            }
        });


    }

    public void search(View view)
    {
        textview2.setText(name);
        dialog= new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        new Jsonmethod().execute("http://api.openweathermap.org/data/2.5/weather?q="+name+"&appid=6d72cb05b1a27c58fe17f07c216c0400");
    }

    public void errorfn(final String stringxx)
    {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),stringxx,Toast.LENGTH_LONG).show();
                imageview.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public class Jsonmethod extends AsyncTask<String,String,String> {


        @Override
        protected String doInBackground(String... params)
        {
            HttpURLConnection connection = null;
            BufferedReader reader=null;

            try {
                URL url= new URL(params[0]);
                connection=(HttpURLConnection)url.openConnection();
                if(connection.getResponseCode()==200)
                    connection.connect();
                else
                    errorfn("ERROR 404  City not found");

                InputStream stream= connection.getInputStream();

                reader= new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer=new StringBuffer();


                String line="";

                while((line=reader.readLine())!=null)
                {
                    buffer.append(line);
                }

                String finaljson= buffer.toString();
                try {
                    JSONObject parentobject=new JSONObject(finaljson);
                    JSONArray weather=parentobject.getJSONArray("weather");
                    JSONObject weatherdetails= weather.getJSONObject(0);
                    String description = weatherdetails.getString("description");
                    imageurl="http://openweathermap.org/img/w/"+weatherdetails.getString("icon")+".png";
                    iconid = weatherdetails.getString("icon");

                    JSONObject maindetails= parentobject.getJSONObject("main");
                    Double temp= maindetails.getDouble("temp");
                    Double pressure=maindetails.getDouble("pressure");
                    Double humidity=maindetails.getDouble("humidity");

                    int cityidno=parentobject.getInt("id");
                    cityid=""+cityidno;

                    String finalresult="Weather condition-"+description+"\n"+"Temperature-"+temp+"K\nHumidity-"+humidity+"%\nPressure-"+pressure+"hpa";
                    return finalresult;


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(connection!=null)
                    connection.disconnect();
                try {
                    if(reader!=null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.hide();
            textView.setText(s);
            textview2.setText(name);
            imageview.setVisibility(View.VISIBLE);
            if(iconid.equals("01d")||iconid.equals("01n"))
            {imageview.setImageResource(R.drawable.x01d);}
            else if(iconid.equals("02d")||iconid.equals("02n"))
                imageview.setImageResource(R.drawable.x02d);
            else if(iconid.equals("03d")||iconid.equals("03n"))
                imageview.setImageResource(R.drawable.x03d);
            else if(iconid.equals("04d")||iconid.equals("04n"))
                imageview.setImageResource(R.drawable.x04d);
            else if(iconid.equals("09d")||iconid.equals("09n"))
                imageview.setImageResource(R.drawable.x09d);
            else if(iconid.equals("10d")||iconid.equals("10n"))
                imageview.setImageResource(R.drawable.x10d);
            else if(iconid.equals("11d")||iconid.equals("11n"))
                imageview.setImageResource(R.drawable.x11d);
            else if(iconid.equals("13d")||iconid.equals("13n"))
                imageview.setImageResource(R.drawable.x013);
            else if(iconid.equals("50d")||iconid.equals("50n"))
                imageview.setImageResource(R.drawable.x50d);

             searchmore.setVisibility(View.VISIBLE);

        }


    }

    public void forecast(View view)
    {
        Intent intent=new Intent(this,futureforecast.class);
        intent.putExtra("cityname",cityid);
        startActivity(intent);
    }

}
