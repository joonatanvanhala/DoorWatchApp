package com.example.doorwatchapp;
import com.amazonaws.services.iot.client.AWSIotDevice;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import android.content.Context;
import android.net.sip.SipSession;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AwsIotConnection {
    private AWSIotDevice device;
    private AWSIotMqttClient client;
    private String clientEndpoint = "a2r9s929yoyxv5-ats.iot.us-east-2.amazonaws.com";
    private String clientId = "phone";
    private String publicCert = "bdb7a3893f.cert.pem";
    private String privateKey = "bdb7a3893f.private.key";

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
            System.out.println("ERROR ATTACH");
            e.printStackTrace();
        }

        System.out.println("CONNECTION STATUS " + client.getConnectionStatus());
    }

    public void publish(){
        String topic = "door/state";
        AWSIotQos qos = AWSIotQos.QOS0;
        String payload = "in";
        long timeout = 3000;                    // milliseconds


        IotMessage message = new IotMessage(topic, qos, payload);
        try {
            client.publish(message, timeout);
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
    }
    public void subscribe(){

        String topicName = "door/picture";
        AWSIotQos qos = AWSIotQos.QOS0;

        IotTopic topic = new IotTopic(topicName, qos);
        try {
            client.subscribe(topic);
            System.out.println("SUBSCRIBED");
        } catch (AWSIotException e) {
            System.out.println("SUBSCRIBE ERROR");
            e.printStackTrace();
        }
    }

}
