package com.example.doorwatchapp;

import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;


import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;

import static com.example.doorwatchapp.MainActivity.image;
import static com.example.doorwatchapp.MainActivity.showViewContent;

public class IotTopic extends AWSIotTopic {
    public IotTopic(String topic, AWSIotQos qos) {
        super(topic, qos);
    }

    @Override
    public void onMessage(AWSIotMessage message) {
        // called when a message is received
        System.out.println("message received");
        System.out.println(message.getStringPayload());
        showViewContent();
        // Parse string and decode it to image
        // Set decoded image as imageView's image
        String base64Image = message.getStringPayload().split(",")[1];
        byte[] imageAsBytes = Base64.decode(base64Image.getBytes(), Base64.DEFAULT);
        image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
    }
}
