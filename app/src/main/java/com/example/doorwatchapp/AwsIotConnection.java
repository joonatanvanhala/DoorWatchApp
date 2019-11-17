package com.example.doorwatchapp;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AwsIotConnection {

    private AWSIotMqttClient client;
    private String clientEndpoint = "a2zvv7ph4lra41-ats.iot.us-east-2.amazonaws.com";
    private String clientId = "DoorWatchAppClient1";
    private String publicCert = "44703cf7e5.cert.pem";
    private String privateKey = "44703cf7e5.private.key";

    public void connect(Context Context) {

        InputStream in1 = null;
        InputStream in2 = null;
        try {
            in1 = Context.getAssets().open(publicCert);
            in2 = Context.getAssets().open(privateKey);

        } catch (IOException e) {
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
            e.printStackTrace();
        }

        SampleUtil.KeyStorePasswordPair pair = SampleUtil.getKeyStorePasswordPair(publicCertFile, privateKeyFile);

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
