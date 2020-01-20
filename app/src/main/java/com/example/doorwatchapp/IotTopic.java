package com.example.doorwatchapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.fasterxml.jackson.databind.Module;

import static com.example.doorwatchapp.MainActivity.hideViewContent;
import static com.example.doorwatchapp.MainActivity.image;
import static com.example.doorwatchapp.MainActivity.yes;


public class IotTopic extends AWSIotTopic {
    public Context context;
    public String base64Image;

    public IotTopic(String topic, AWSIotQos qos) {
        super(topic, qos);
    }

    public void Setcontext(Context context) {
        this.context = context;
    }

    @Override
    public void onMessage(AWSIotMessage message) {
        // called when a message is received
        System.out.println("message received");
        // Parse string and decode it to image
        // Set decoded image as imageView's image
        base64Image = message.getStringPayload();
        System.out.println(base64Image);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                yes.post(new
                 Runnable() {
                     @Override
                     public void run() {
                         //String base1 = base64Image.split(",")[1];
                         byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
                         image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                     }
                 });
            }
        });
        thread.start();
    }
}
