package com.example.doorwatchapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;


import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;

import static com.example.doorwatchapp.MainActivity.image;
import static com.example.doorwatchapp.MainActivity.showViewContent;
import static com.example.doorwatchapp.MainActivity.yes;
import org.json.*;

public class IotTopic extends AWSIotTopic {
    public Context context;
    public String jsonBase64Image;
    public String parsedBase64Image;

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
        jsonBase64Image = message.getStringPayload();
        try {
            JSONObject reader = new JSONObject(jsonBase64Image);
            parsedBase64Image = reader.getString("picture");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                yes.post(new
                 Runnable() {
                     @Override
                     public void run() {
                         //String base1 = base64Image.split(",")[1];
                         showViewContent();
                         byte[] imageAsBytes = Base64.decode(parsedBase64Image.getBytes(), Base64.DEFAULT);
                         image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                     }
                 });
            }
        });
        thread.start();
    }
}
