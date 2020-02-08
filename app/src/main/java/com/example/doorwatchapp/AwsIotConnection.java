package com.example.doorwatchapp;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AwsIotConnection {
    private AWSIotMqttClient client;
    private String clientEndpoint = "a2r9s929yoyxv5-ats.iot.us-east-2.amazonaws.com";
    private String clientId = "phone";
    private String publicCert = "bdb7a3893f.cert.pem";
    private String privateKey = "bdb7a3893f.private.key";
    private IotTopic topic;

    public void connect(Context Context) {

        InputStream in1 = null;
        InputStream in2 = null;
        try {
            in1 = Context.getAssets().open(publicCert);
            in2 = Context.getAssets().open(privateKey);

        } catch (IOException e) {
            Toast.makeText(MainActivity.context, "Cannot locate Iot client certificates", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        String publicCertFile = Context.getFilesDir() + "/" + publicCert;
        String privateKeyFile = Context.getFilesDir() + "/" + privateKey;

        try {
            if(!new File(publicCertFile).exists()){
                Files.copy(in1, Paths.get(publicCertFile));
            }
            if(!new File(privateKeyFile).exists()){
                Files.copy(in2, Paths.get(privateKeyFile));
            }

        } catch (IOException e) {
            Toast.makeText(MainActivity.context, "Cannot copy Iot client certificates", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        SampleUtil.KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(publicCertFile, privateKeyFile);

        client = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);

        try {
            client.connect();
        } catch (AWSIotException e) {
            Toast.makeText(MainActivity.context, "Error connecting device to Iot Network: " + client.getConnectionStatus(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        Toast.makeText(MainActivity.context, "IoT connection status: " + client.getConnectionStatus(), Toast.LENGTH_LONG).show();
        System.out.println("CONNECTION STATUS " + client.getConnectionStatus());
    }


    public void publish(String buttonMessage){
        String topic = "door/state";
        AWSIotQos qos = AWSIotQos.QOS0;
        String payload = buttonMessage;
        long timeout = 3000;                    // milliseconds

        IotMessage message = new IotMessage(topic, qos, payload);
        try {
            client.publish(message, timeout);
            if(buttonMessage.equals("in")){
                Toast.makeText(MainActivity.context, "Opening door!", Toast.LENGTH_LONG).show();
            }
            else if(buttonMessage.equals("out")){
                Toast.makeText(MainActivity.context, "Door will not be opened", Toast.LENGTH_LONG).show();
            }
        } catch (AWSIotException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.context, "Unable to publish message" , Toast.LENGTH_LONG).show();
        }
    }
    public IotTopic subscribe(){

        String topicName = "door/picture";
        AWSIotQos qos = AWSIotQos.QOS0;
        topic = new IotTopic(topicName, qos);
        try {
            client.subscribe(topic);
            System.out.println("SUBSCRIBED");
        } catch (AWSIotException e) {
            System.out.println("ouio");
            Toast.makeText(MainActivity.context, "Unable to subscribe to a topic", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return topic;
    }
}
