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


//TODO: create eventlistener which triggers if image/message is received from door camera subscription
//TODO: add base64 encoder
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
        iotConnection.subscribe();
        iotConnection.publish();

    }


}
