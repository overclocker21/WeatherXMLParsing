package com.androbro.weatherxmlparsing;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;



public class MainActivity extends AppCompatActivity {

    private Menu mymenu;
    private TextView stationIdTV;
    private TextView observation_timeTV;
    private TextView weatherTV;
    private TextView temperatureTV;
    private TextView windTV;
    private ProgressDialog dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stationIdTV = (TextView) findViewById(R.id.station_id);
        observation_timeTV = (TextView) findViewById(R.id.observation_time);
        weatherTV = (TextView) findViewById(R.id.weather);
        temperatureTV = (TextView) findViewById(R.id.temperature_string);
        windTV = (TextView) findViewById(R.id.wind_string);
        //updating our data with Progress Dialog
        //it will be shown just once at the start of the app
        updateDataWithProgressDialog();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mymenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.refresh){
            //setting up action bar menu item and animation for it
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ImageView iv = (ImageView)inflater.inflate(R.layout.iv_refresh, null);
            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate_refresh);
            rotation.setRepeatCount(Animation.INFINITE);
            iv.startAnimation(rotation);
            item.setActionView(iv);

            //when pressed refresh button will sping for the duration of the background data updating process:
            updateDataWithAnimation();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void resetUpdating()
    {
        // Get our refresh item from the menu
        MenuItem m = mymenu.findItem(R.id.refresh);
        if(m.getActionView()!=null)
        {
            // Remove the animation.
            m.getActionView().clearAnimation();
            m.setActionView(null);
        }
    }


    public void updateDataWithProgressDialog(){
        AsyncTaskWithDialog task = new AsyncTaskWithDialog(stationIdTV, observation_timeTV, weatherTV, temperatureTV, windTV);
        task.execute();
    }

    public void updateDataWithAnimation(){
        AsyncTaskWithAnimation asyncTaskWithAnimation = new AsyncTaskWithAnimation(this, stationIdTV, observation_timeTV, weatherTV, temperatureTV, windTV );
        asyncTaskWithAnimation.execute();
    }

    public class AsyncTaskWithDialog extends AsyncTask<Void, Void, NodeList>{


        NodeList list = null;

        public AsyncTaskWithDialog(TextView tv1, TextView tv2, TextView tv3, TextView tv4, TextView tv5){

            stationIdTV = tv1;
            observation_timeTV = tv2;
            weatherTV = tv3;
            temperatureTV = tv4;
            windTV = tv5;
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("Loading…");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected NodeList doInBackground(Void... params) {
            try {
                //making a thread sleep just to show that animation and dialog are working
                Thread.sleep(2000);
                list = ((new WeatherHttpClient()).returnNodes());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return list;

        }

        @Override
        protected void onPostExecute(NodeList list) {
            if (dialog.isShowing()) {
                dialog.dismiss();
                processNodeList(list);
            }
        }
    }


    public class AsyncTaskWithAnimation extends AsyncTask<Void, Void, NodeList>{

        private Context mCon;

        NodeList list = null;

        public AsyncTaskWithAnimation(Context con, TextView tv1, TextView tv2, TextView tv3, TextView tv4, TextView tv5){

            mCon = con;
            stationIdTV = tv1;
            observation_timeTV = tv2;
            weatherTV = tv3;
            temperatureTV = tv4;
            windTV = tv5;
        }

        @Override
        protected NodeList doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
                list = ((new WeatherHttpClient()).returnNodes());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return list;

        }

        @Override
        protected void onPostExecute(NodeList list) {
            processNodeList(list);
            ((MainActivity) mCon).resetUpdating();
            Toast.makeText(getApplicationContext(), "Data updated!", Toast.LENGTH_SHORT).show();
        }
    }

    public void processNodeList(NodeList list){
        if (list != null && list.getLength() > 0) {
            for (int i = 0; i < list.getLength(); i++) {
                if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) list.item(i);
                    if (element.getNodeName().equals("station_id")) {

                        String stationId = element.getTextContent();
                        stationIdTV.setText("Station ID: " + stationId);
                        MyLogger.m("" + stationId);

                    } else if (element.getNodeName().equals("observation_time")) {

                        String observationTime = element.getTextContent();
                        observation_timeTV.setText("Observation time: " + observationTime);
                        MyLogger.m("" + observationTime);

                    } else if (element.getNodeName().equals("weather")) {

                        String weather = element.getTextContent();
                        weatherTV.setText("Weather: " + weather);
                        MyLogger.m("" + weather);

                    } else if (element.getNodeName().equals("temperature_string")) {

                        String tempString = element.getTextContent();
                        temperatureTV.setText("Temperature: " + tempString);
                        MyLogger.m("" + tempString);

                    } else if (element.getNodeName().equals("wind_string")) {

                        String windString = element.getTextContent();
                        windTV.setText("Wind: " + windString);
                        MyLogger.m("" + windString);

                    }
                }
            }
        }
    }

}