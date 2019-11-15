package com.example.doorwatchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;


public class MainActivity extends AppCompatActivity {
    private Button mButton;
    private AWSIotMqttClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }
    public void connect(){

        String clientEndpoint = "<prefix>.iot.<region>.amazonaws.com";       // replace <prefix> and <region> with your own
        String clientId = "<unique client id>";                              // replace with your own client ID. Use unique client IDs for concurrent connections.
        String certificateFile = "<certificate file>";                       // X.509 based certificate file
        String privateKeyFile = "<private key file>";                        // PKCS#1 or PKCS#8 PEM encoded private key file

        //TODO: load certificate files and pass it to keystore and keypassword
        //client = new AWSIotMqttClient(clientEndpoint, clientId, keyStore, keyPassword);
        try {
            client.connect();
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
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
        String topicName = "my/own/topic";
        AWSIotQos qos = AWSIotQos.QOS0;

        IotTopic topic = new IotTopic(topicName, qos);
        try {
            client.subscribe(topic);
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
    }

}
