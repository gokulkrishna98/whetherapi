package com.example.gokulkrishnam.whetherapi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;

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

public class futureforecast extends AppCompatActivity {

    String cityname;
    TextView textView;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futureforecast2);
        Intent intent=getIntent();
        cityname=intent.getStringExtra("cityname");
        Log.d("vivz", "onCreate: "+cityname);
        textView=(TextView)findViewById(R.id.textView3);
        String urlid="http://api.openweathermap.org/data/2.5/forecast?id="+cityname+"&appid=6d72cb05b1a27c58fe17f07c216c0400";

        new Jsonmethod().execute("http://api.openweathermap.org/data/2.5/forecast?id="+cityname+"&appid=6d72cb05b1a27c58fe17f07c216c0400");

    }

    public class Jsonmethod extends AsyncTask<String,String,String> {


        @Override
        protected String doInBackground(String... params)
        {   Log.d("vivz", "doInBackground: hello");
            HttpURLConnection connection = null;
            BufferedReader reader=null;
            Log.d("vivz", "doInBackground: hello");
            try {
                URL url= new URL(params[0]);
                connection=(HttpURLConnection)url.openConnection();
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

                    if(parentobject.getString("cod").equals("404"))
                    {
                        return "ERROR 404 website not found";
                    }
                    JSONArray list=parentobject.getJSONArray("list");
                    JSONObject day1=list.getJSONObject(0);
                    JSONObject day2=list.getJSONObject(8);
                    JSONObject day3=list.getJSONObject(16);
                    JSONObject day4=list.getJSONObject(24);
                    JSONObject day5=list.getJSONObject(32);



                    String finalresult;
                        String dt=day1.getString("dt_txt");
                        JSONObject maininfo=day1.getJSONObject("main");
                        Double t=maininfo.getDouble("temp");
                        Double p=maininfo.getDouble("pressure");
                        Double h=maininfo.getDouble("humidity");



                    String dt2=day2.getString("dt_txt");
                    JSONObject maininfo2=day2.getJSONObject("main");
                    Double t2=maininfo2.getDouble("temp");
                    Double p2=maininfo2.getDouble("pressure");
                    Double h2=maininfo2.getDouble("humidity");

                    String dt3=day3.getString("dt_txt");
                    JSONObject maininfo3=day3.getJSONObject("main");
                    Double t3=maininfo3.getDouble("temp");
                    Double p3=maininfo3.getDouble("pressure");
                    Double h3=maininfo3.getDouble("humidity");

                    String resultday3="\n\ndate:-"+dt3+"\ntemperature-"+t3+"K\nhumidity-"+h3+"%\npressure-"+p3;
                    String dt4=day4.getString("dt_txt");
                    JSONObject maininfo4=day4.getJSONObject("main");
                    Double t4=maininfo4.getDouble("temp");
                    Double p4=maininfo4.getDouble("pressure");
                    Double h4=maininfo4.getDouble("humidity");
                    String resultday4="\n\ndate:-"+dt4+"\ntemperature-"+t4+"K\nhumidity-"+h4+"%\npressure-"+p4;

                    String dt5=day5.getString("dt_txt");
                    JSONObject maininfo5=day5.getJSONObject("main");
                    Double t5=maininfo5.getDouble("temp");
                    Double p5=maininfo5.getDouble("pressure");
                    Double h5=maininfo5.getDouble("humidity");
                    String resultday5="\n\ndate:-"+dt5+"\ntemperature-"+t5+"K\nhumidity-"+h5+"%\npressure-"+p5;


                        finalresult="date:-"+dt+"\ntemperature-"+t+"K\nhumidity-"+h+"%\npressure-"+p+"\n\ndate:-"+dt2+"\ntemperature-"+t2+"K\nhumidity-"+h2+"%\npressure-"+p2+resultday3+resultday4+resultday5;

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
            textView.setText(s);

        }


    }


}
