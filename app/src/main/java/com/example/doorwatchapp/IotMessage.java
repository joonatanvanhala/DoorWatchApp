package com.example.doorwatchapp;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;

public class IotMessage extends AWSIotMessage {
    public IotMessage(String topic, AWSIotQos qos, String payload) {
        super(topic, qos, payload);
    }

    @Override
    public void onSuccess() {
        // called when message publishing succeeded

        System.out.println("MESSAGE PUBLISHED");
    }

    @Override
    public void onFailure() {
        // called when message publishing failed
        System.out.println("MESSAGE FAILURE");

    }

    @Override
    public void onTimeout() {
        // called when message publishing timed out
        System.out.println("MESSAGE TIMEOUT");

    }
}
