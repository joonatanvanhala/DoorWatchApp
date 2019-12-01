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
    private String clientEndpoint = "a2zvv7ph4lra41-ats.iot.us-east-2.amazonaws.com";
    private String clientId = "GG_TrafficLight";
    private String publicCert = "082dbff0f7.cert.pem";
    private String privateKey = "082dbff0f7.private.key";

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
        String thingName = "GG_TrafficLight";

        device = new AWSIotDevice(thingName);

        try {

            client.connect();
            client.attach(device);
        } catch (AWSIotException e) {
            System.out.println("ERROR ATTACH");
            e.printStackTrace();
        }

        System.out.println("CONNECTION STATUS " + client.getConnectionStatus());
    }

    public void publish(String buttonMessage){
        String topic = "my/own/topic";
        AWSIotQos qos = AWSIotQos.QOS0;
        String payload = buttonMessage;
        long timeout = 3000;                    // milliseconds


        IotMessage message = new IotMessage(topic, qos, payload);
        try {
            client.publish(message, timeout);
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
    }
    public void subscribe(){

        String topicName = "GG_TrafficLight";
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


    public String deviceGet(){
        try {
            String msg = device.get();
            return msg;
        } catch (AWSIotException e) {
            System.out.println("error device");
            e.printStackTrace();
            return null;
        }
    }
    public void deviceUpdate(){
        //String state = "{\"state\":{\"reported\":{\"property\":true}}}";
        String state = "{\"state\":{\"desired\":{\"property\":\"G\"}}}";


        try {
            device.update(state);
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
    }


}