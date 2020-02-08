package com.example.doorwatchapp;


import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import static com.example.doorwatchapp.MainActivity.setImage;

import org.json.*;

public class IotTopic extends AWSIotTopic {
    private String jsonBase64Image;

    public IotTopic(String topic, AWSIotQos qos) {
        super(topic, qos);
    }

    // called when a message is received
    @Override
    public void onMessage(AWSIotMessage message) {

        jsonBase64Image = message.getStringPayload();
        setImage(jsonBase64Image);
    }
}
