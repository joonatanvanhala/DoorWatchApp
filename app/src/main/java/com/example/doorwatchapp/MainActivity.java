package com.example.doorwatchapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.core.AwsIotCompletion;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.spec.PKCS8EncodedKeySpec;


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
    }
    public void connect() {

        String clientEndpoint = "ajllz5y3u98na-ats.iot.us-west-2.amazonaws.com";       // replace <prefix> and <region> with your own
        String clientId = "DoorWatchAppClient1";                              // replace with your own client ID. Use unique client IDs for concurrent connections.
        String certificateFile = "/Users/joonatanvanhala/AndroidStudioProjects/DoorWatchApp/app/src/main/java/com/example/doorwatchapp/f524ac081d-certificate.pem.crt";                       // X.509 based certificate file
        String privateKeyFile = "/Users/joonatanvanhala/AndroidStudioProjects/DoorWatchApp/app/src/main/java/com/example/doorwatchapp/f524ac081d-private.pem.key";                        // PKCS#1 or PKCS#8 PEM encoded private key file

        SampleUtil.KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(certificateFile, privateKeyFile);
        client = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
        try {
            System.out.println("Connecting");
            client.connect();
        } catch (AWSIotException e) {
            System.out.println("ERROR");
            e.printStackTrace();
        }
        Toast.makeText(MainActivity.this, "This is my Toast message!" + client.getConnectionStatus(),
                Toast.LENGTH_LONG).show();
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
