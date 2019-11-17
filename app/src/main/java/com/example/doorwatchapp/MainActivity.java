package com.example.doorwatchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


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
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(MainActivity.this, "This is my Toast message!",
                        Toast.LENGTH_LONG).show();
            }
        });
        //Create new connection to aws iot service
        AwsIotConnection iotConnection = new AwsIotConnection();
        iotConnection.connect(getApplicationContext());
        //subscribe to required topic(s)
        iotConnection.subscribe();
        //wait for message from doorcam client

        //publish users decision
    }

}
