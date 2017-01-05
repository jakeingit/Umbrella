package com.nerdery.umbrella.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nerdery.umbrella.R;
import com.nerdery.umbrella.api.ApiManager;
import com.nerdery.umbrella.api.IconApi;
import com.nerdery.umbrella.model.CurrentObservation;
import com.nerdery.umbrella.model.WeatherData;
import com.squareup.picasso.Picasso;

import java.sql.SQLOutput;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private SharedPreferences.Editor e;
    //private WeatherData data;
    private ApiManager api;
    public  IconApi Iapi;
    int zip = 55437;



    @Override
    protected void onPostResume() {
        super.onPostResume();
        try {
            initSP();
            fetchWeather();
        }
        catch(Exception e){
            sp.edit().putInt("zip",55403).commit();
            System.out.println("Faulty Zipcode");
            Toast.makeText(MainActivity.this,"Invalid Zipcode", Toast.LENGTH_LONG).show();
        }



        if (sp.getBoolean("firstrun", true)) {
            onCreateDialog();
            sp.edit().putBoolean("firstrun", false).commit();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().show();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#222222")));




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                System.out.println("[Debug] settings");
                onCreateDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void changeColors(WeatherData weatherData) {
        RelativeLayout main = (RelativeLayout) findViewById(R.id.currentobservationlayout);

        if (weatherData.currentObservation.tempFahrenheit >= 60) {
            main.setBackgroundColor(Color.parseColor("#ff9800"));

        } else {
            main.setBackgroundColor(Color.parseColor("#03a9f4"));
        }
    }

    public void initSP(){
        sp =  PreferenceManager.getDefaultSharedPreferences(this);
        e = sp.edit();
        zip = sp.getInt("zip", 55437);
    }

    public void fetchWeather(){
        System.out.println("=============== FETCH =================");
        api = new ApiManager();
        Iapi = new ApiManager().getIconApi();

        Call call;
        try {
             call = api.getWeatherApi().getForecastForZip(zip);
        }
        catch(Exception e){
            System.out.println(e);
            System.out.println("Faulty Zipcode");
            Toast.makeText(MainActivity.this, "Invalid Zipcode", Toast.LENGTH_LONG).show();
            Toast.makeText(MainActivity.this, "Setting Zipcode to Minneapolis", Toast.LENGTH_LONG).show();
            zip = 55403;
            sp.edit().putInt("zip", 55403).commit();
        }finally {
            call = api.getWeatherApi().getForecastForZip(zip);
        }

        try{


        call.enqueue(new Callback() {
            @Override
            public void onResponse(Response response) {
                response.toString();
                //retuns a response
                WeatherData weatherData = saveWeatherData(response);
                setData(weatherData);
                changeColors(weatherData);
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                findViewById(R.id.icon).setVisibility(View.VISIBLE);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });


            //System.out.println("INFO "+r);
        }
        catch(Exception e){
            System.out.println(e);
        }

    }

    public WeatherData saveWeatherData(Response response){
        return (WeatherData) response.body();

    }

    public void setImage(String weather, ImageView imageview){

        System.out.println("Setting Image");
        switch(weather.toLowerCase()){
            case "chanceflurries":
                Picasso.with(MainActivity.this).load("http://orig14.deviantart.net/9f2d/f/2011/344/0/b/snow_cloud_cutie_mark_by_kinnichi-d4ip53j.png").into(imageview);
                break;

            case "chancerain":
                Picasso.with(MainActivity.this).load("http://www.clipartkid.com/images/83/clouds-clipart-black-and-white-rain-cloud-black-white-png-2GcgYz-clipart.png").into(imageview);
                break;

            case "chancesleet":
                Picasso.with(MainActivity.this).load("http://orig14.deviantart.net/9f2d/f/2011/344/0/b/snow_cloud_cutie_mark_by_kinnichi-d4ip53j.png").into(imageview);
                break;


            case "chancesnow":
                Picasso.with(MainActivity.this).load("http://orig14.deviantart.net/9f2d/f/2011/344/0/b/snow_cloud_cutie_mark_by_kinnichi-d4ip53j.png").into(imageview);
                break;


            case "chancetstorms":
                Picasso.with(MainActivity.this).load("http://www.clipartkid.com/images/594/thunder-cloud-clipart-black-and-white-cloud-clip-art-black-and-white-6NvDW1-clipart.png").into(imageview);
                break;

            case "clear":
                Picasso.with(MainActivity.this).load("http://wcrd.net/wp-content/themes/wcrd/weather/images/icons/Large/Sunny.png").into(imageview);

                break;


            case "flurries":
                Picasso.with(MainActivity.this).load("http://orig14.deviantart.net/9f2d/f/2011/344/0/b/snow_cloud_cutie_mark_by_kinnichi-d4ip53j.png").into(imageview);

                break;

            case "fog":
                Picasso.with(MainActivity.this).load("http://nobacks.com/wp-content/uploads/2014/11/Cloud-26.png").into(imageview);

                break;

            case "hazy":
                Picasso.with(MainActivity.this).load("http://nobacks.com/wp-content/uploads/2014/11/Cloud-26.png").into(imageview);

                break;

            case "mostlycloudy":
                Picasso.with(MainActivity.this).load("http://nobacks.com/wp-content/uploads/2014/11/Cloud-26.png").into(imageview);

                break;

            case "mostlysunny":
                Picasso.with(MainActivity.this).load("http://wcrd.net/wp-content/themes/wcrd/weather/images/icons/Large/Sunny.png").into(imageview);

                break;

            case "partlycloudy":
                Picasso.with(MainActivity.this).load("http://nobacks.com/wp-content/uploads/2014/11/Cloud-26.png").into(imageview);

                break;

            case "partlysunny":
                Picasso.with(MainActivity.this).load("http://wcrd.net/wp-content/themes/wcrd/weather/images/icons/Large/Sunny.png").into(imageview);

                break;

            case "sleet":
                Picasso.with(MainActivity.this).load("http://orig14.deviantart.net/9f2d/f/2011/344/0/b/snow_cloud_cutie_mark_by_kinnichi-d4ip53j.png").into(imageview);

                break;

            case "rain":
                Picasso.with(MainActivity.this).load("http://www.clipartkid.com/images/83/clouds-clipart-black-and-white-rain-cloud-black-white-png-2GcgYz-clipart.png").into(imageview);

                break;

            case "snow":
                Picasso.with(MainActivity.this).load("http://orig14.deviantart.net/9f2d/f/2011/344/0/b/snow_cloud_cutie_mark_by_kinnichi-d4ip53j.png").into(imageview);

                break;

            case "sunny":
                Picasso.with(MainActivity.this).load("http://wcrd.net/wp-content/themes/wcrd/weather/images/icons/Large/Sunny.png").into(imageview);

                break;

            case "tstorms":
                Picasso.with(MainActivity.this).load("http://www.clipartkid.com/images/594/thunder-cloud-clipart-black-and-white-cloud-clip-art-black-and-white-6NvDW1-clipart.png").into(imageview);

                break;

            case "cloudy":
                Picasso.with(MainActivity.this).load("http://nobacks.com/wp-content/uploads/2014/11/Cloud-26.png").into(imageview);

                break;

            default:

                //unable to load off Async task
                Picasso.with(MainActivity.this).load("http://nobacks.com/wp-content/uploads/2014/11/Cloud-26.png").into(imageview);
                break;
        }
    }

    public void setData(WeatherData weatherData){
        try {
            //use CurrentObservation for the first half
            CurrentObservation current = new CurrentObservation();


            //current.displayLocation = "minneapolis";
            String displayLocation = weatherData.currentObservation.displayLocation.city;
            Float tempFahrenheit = weatherData.currentObservation.tempFahrenheit;
            Float tempCelsius = weatherData.currentObservation.tempCelsius;
            String weather = weatherData.currentObservation.weather;
            String icon = Iapi.getUrlForIcon(weather, false);
            //Manipulate the Icon
            //Iapi.getUrlForIcon(weather, false);
            //SET using URL

            Boolean metric = sp.getBoolean("metric", false);

            TextView loc = (TextView) findViewById(R.id.location);
            TextView temp = (TextView) findViewById(R.id.temp);
            TextView weathert = (TextView) findViewById(R.id.weather);
            ImageView ico = (ImageView) findViewById(R.id.icon);

            setImage(weather, ico);
            //Picasso.with(MainActivity.this).load(icon).into(ico);
            //ico.setBackground(icon);

            loc.setText(displayLocation);

            float highesttemp = -Float.MAX_VALUE;
            float lowesttemp = Float.MAX_VALUE;

            if (metric) {
                temp.setText(String.valueOf(tempCelsius) + "\u2103");

                for (int i = 0; i < 23; i++) {
                    if (weatherData.forecast.get(i).tempCelsius > highesttemp) {
                        highesttemp = weatherData.forecast.get(i).tempCelsius;
                    }
                    if (weatherData.forecast.get(i).tempCelsius < lowesttemp) {
                        lowesttemp = weatherData.forecast.get(i).tempCelsius;
                    }
                }
            } else {
                temp.setText(String.valueOf(tempFahrenheit) + "\u2109");

                for (int i = 0; i < 23; i++) {
                    if (weatherData.forecast.get(i).tempFahrenheit > highesttemp) {
                        highesttemp = weatherData.forecast.get(i).tempFahrenheit;
                    }
                    if (weatherData.forecast.get(i).tempFahrenheit < lowesttemp) {
                        lowesttemp = weatherData.forecast.get(i).tempFahrenheit;
                    }
                }
            }

            TextView highs = (TextView) findViewById(R.id.high);
            TextView lows = (TextView) findViewById(R.id.lows);

            highs.setText(String.valueOf(highesttemp));
            lows.setText(String.valueOf(lowesttemp));

            weathert.setText(weather);

            //set ICON :)

            //Update Forecast!

            for (int i = 0; i < 23; i++) {
                if (weatherData.forecast.get(i).tempFahrenheit > highesttemp) {
                    highesttemp = weatherData.forecast.get(i).tempFahrenheit;
                }
                if (weatherData.forecast.get(i).tempFahrenheit < lowesttemp) {
                    lowesttemp = weatherData.forecast.get(i).tempFahrenheit;
                }
            }
        }catch(Exception e){
            sp.edit().putInt("zip", 55403).commit();
            TextView warning = (TextView) findViewById(R.id.weather);
            TextView warning2 = (TextView) findViewById(R.id.location);
            TextView warning3 = (TextView) findViewById(R.id.temp);
            warning2.setVisibility(View.GONE);
            warning3.setVisibility(View.GONE);

            warning.setText("Invalid ZIPCODE");
            Toast.makeText(MainActivity.this, "Error in Zipcode, Force Restart", Toast.LENGTH_LONG).show();
            try{
                Thread.sleep(5000);
            }
            catch(Exception ex){
                //oh god please dont see this code its terrible but toast isnt showing
            }
            MainActivity.this.finish();
        }

    }


    public void onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        // Get the layout inflater
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();

        final View inflate = inflater.inflate(R.layout.settingsdialog, null);

        //sets the dialog
        Boolean metric = false;

        RadioButton e_F = (RadioButton)inflate.findViewById(R.id.F);
        RadioButton e_C = (RadioButton)inflate.findViewById(R.id.C);
        EditText e_zipcode = (EditText)inflate.findViewById(R.id.zipcode);
        metric = sp.getBoolean("metric", false);
        int zipcode = sp.getInt("zip", 55437);


        e_zipcode.setText(String.valueOf(zipcode));

        if(metric)
        {
            e_C.setChecked(true);
        }
        else
        {
            e_F.setChecked(true);
        }


        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        builder.setView(inflate)
                // Add action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        EditText e_zipcode = (EditText) inflate.findViewById(R.id.zipcode);
                        int zipcode = Integer.valueOf(e_zipcode.getText().toString());

                        RadioButton e_F = (RadioButton) inflate.findViewById(R.id.F);
                        RadioButton e_C = (RadioButton) inflate.findViewById(R.id.C);
                        Boolean metric = false;

                        if (e_C.isChecked()) {
                            metric = true;
                        } else {
                            metric = false;
                        }
                        System.out.println("111 metric is " + metric);
                        System.out.println("Cel is checked+ " + e_C.isChecked());
                        if(zipcode < 10000 || zipcode > 99999)
                        {

                            Toast.makeText(MainActivity.this,"Invalid 5 Digit Zipcode", Toast.LENGTH_LONG).show();

                            Toast.makeText(MainActivity.this,"Defaulting to Minneapolis", Toast.LENGTH_LONG).show();
                            zipcode = 55403;
                        }
                            zip = zipcode;

                        sp.edit()
                                .putInt("zip", zipcode)
                                .putBoolean("metric", metric)
                                .commit();
                        fetchWeather();

                        System.out.println("Saved");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //does nothing
                        System.out.println("Not Saved");

                    }
                });
         builder.create();



        builder.show();


    }
}
