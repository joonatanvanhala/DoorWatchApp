package com.example.doorwatchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;



public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private AWSIotMqttClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        System.out.println("START");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connect();
        mButton = findViewById(R.id.button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // Do something
                Toast.makeText(MainActivity.this, "This is my Toast message!",
                        Toast.LENGTH_LONG).show();
            }
        });
        subscribe();
    }
    public void connect() {

        String clientEndpoint = "a2zvv7ph4lra41-ats.iot.us-east-2.amazonaws.com";       // replace <prefix> and <region> with your own
        String clientId = "DoorWatchAppClient1";                              // replace with your own client ID. Use unique client IDs for concurrent connections.
        String publicCert = "44703cf7e5.cert.pem";
        String privateKey = "44703cf7e5.private.key";

        //get files
        InputStream in1 = null;
        InputStream in2 = null;
        try {
             in1 = getApplicationContext().getAssets().open(publicCert);
             in2 = getApplicationContext().getAssets().open(privateKey);

        } catch (IOException e) {
            e.printStackTrace();
        }
        String publicCertFile = getFilesDir() + "/" + publicCert;
        String privateKeiFile = getFilesDir() + "/" + privateKey;

        try {
            Files.copy(in1, Paths.get(publicCertFile));
            Files.copy(in2, Paths.get(privateKeiFile));

        } catch (IOException e) {
            e.printStackTrace();
        }

        //connect
        SampleUtil.KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(publicCertFile, privateKeiFile);

        client = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
        try {
            client.connect();
        } catch (AWSIotException e) {
            System.out.println("CONNECTION ERROR " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        System.out.println("CONNECTION STATUS " + client.getConnectionStatus());
    }

    public void publish(){
        String topic = "my/own/topic";
        AWSIotQos qos = AWSIotQos.QOS0;
        String payload = "any payload";
        long timeout = 3000;                    // milliseconds


        IotMessage message = new IotMessage(topic, qos, payload);
        try {
            client.publish(message, timeout);
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
    }
    public void subscribe(){
        String topicName = "$aws/things/GG_TrafficLight/shadow/update";
        AWSIotQos qos = AWSIotQos.QOS0;

        IotTopic topic = new IotTopic(topicName, qos);
        try {
            client.subscribe(topic);
            System.out.println("SUBSCRIBE");
        } catch (AWSIotException e) {
            System.out.println("SUBSCRIBE ERROR");
            e.printStackTrace();
        }
    }


}
