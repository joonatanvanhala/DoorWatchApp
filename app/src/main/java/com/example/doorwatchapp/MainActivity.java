package com.example.doorwatchapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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

        //get files
        InputStream in1 = null;
        InputStream in2 = null;
        try {
             in1 = getApplicationContext().getAssets().open( "f524ac081d-certificate.pem.crt" );
             in2 = getApplicationContext().getAssets().open( "f524ac081d-private.pem.key" );

        } catch (IOException e) {
            e.printStackTrace();
        }
        String outputFile1 = getFilesDir() + "/f524ac081d-certificate.pem.crt";
        String outputFile2 = getFilesDir() + "/f524ac081d-private.pem.key";

        try {
            Files.copy(in1, Paths.get(outputFile1));
            Files.copy(in2, Paths.get(outputFile2));

        } catch (IOException e) {
            e.printStackTrace();
        }

        //connect
        SampleUtil.KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(outputFile1, outputFile2);

           client = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
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
