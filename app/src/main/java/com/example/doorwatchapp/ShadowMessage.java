package com.example.doorwatchapp;

import com.amazonaws.services.iot.client.AWSIotMessage;

public class ShadowMessage extends AWSIotMessage {
    public ShadowMessage() {
        super(null, null);
    }

    @Override
    public void onSuccess() {
        System.out.println("SUCCESS SHADOW");
        // called when the shadow method succeeded
        // state (JSON document) received is available in the payload field
    }

    @Override
    public void onFailure() {
        System.out.println("ERROR SHADOW");

        // called when the shadow method failed
    }

    @Override
    public void onTimeout() {
        System.out.println("TIMEOUT SHADOW");

        // called when the shadow method timed out
    }
}
