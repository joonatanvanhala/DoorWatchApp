package com.example.doorwatchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.amazonaws.services.iot.client.AWSIotMqttClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//TODO: test subscribe and publish functionality
//TODO: create eventlistener which triggers if image/message is received from door camera subscription
//TODO: add UI decision functionality for publishing door status
//TODO: publish users decision

public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private AWSIotMqttClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        System.out.println("START");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mButton = findViewById(R.id.button);


        //Create new connection to aws iot service
        AwsIotConnection iotConnection = new AwsIotConnection();
        iotConnection.connect(getApplicationContext());
        while(true)
        {
            //get messages from device
            String msg = iotConnection.deviceGet();
            //msg received
            if(msg != null){
                try {
                    JSONObject reader = new JSONObject(msg);
                    JSONObject state  = reader.getJSONObject("state");
                    JSONObject desired  = state.getJSONObject("desired");
                    String property  = desired.getString("property");
                    System.out.println(property);
                    if(property.equals("R")){
                        System.out.println("IS R");
                        mButton.setVisibility(View.VISIBLE);
                        Toast.makeText(MainActivity.this, "This is IoT Application!",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            //sleep
            else{
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


        }

    }


}
