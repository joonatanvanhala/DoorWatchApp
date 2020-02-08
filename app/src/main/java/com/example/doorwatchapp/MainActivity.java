package com.example.doorwatchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private AwsIotConnection iotConnection;
    private static TextView text_view_id;
    private static Button no;
    public static Button yes;
    public static ImageView image;
    public static Context context;
    private static String parsedBase64Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize messages array
        //get and initialize view content
        text_view_id = findViewById(R.id.text_view_id);
        no = findViewById(R.id.no_button);
        yes = findViewById(R.id.yes_button);
        image = this.findViewById(R.id.image);
        context = this.getApplicationContext();
        //set button texts
        initViewContent();
        //initially hide content
        hideViewContent();
        //Create new connection to aws iot service
        iotConnection = new AwsIotConnection();
        iotConnection.connect(getApplicationContext());
        iotConnection.subscribe();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    public void openDoor(View v) {
        String message = "in";
        iotConnection.publish(message);
        //Remove image from view after user has made selection
        image = findViewById(R.id.image);
        image.setImageDrawable(null);
        hideViewContent();
    }

    public void closeDoor(View v) {
        String message = "out";
        iotConnection.publish(message);
        //Remove image from view after user has made selection
        image = findViewById(R.id.image);
        image.setImageDrawable(null);
        hideViewContent();
    }
    private static void hideViewContent() {
        text_view_id.setText("Ei ketään ovella");
        no.setVisibility(View.INVISIBLE);
        yes.setVisibility(View.INVISIBLE);
        image.setVisibility(View.INVISIBLE);
    }
    private static void showViewContent() {
        text_view_id.setText("Avataanko ovi?");
        no.setVisibility(View.VISIBLE);
        yes.setVisibility(View.VISIBLE);
        image.setVisibility(View.VISIBLE);
    }
    private static void initViewContent(){
        yes.setText("Kyllä");
        no.setText("Ei");
    }
    public static void setImage(final String jsonBase64Image){
        //create new thread for updating view content while application is running
        try {
            JSONObject reader = new JSONObject(jsonBase64Image);
            parsedBase64Image = reader.getString("picture");
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    image.post(new
                                       Runnable() {
                                           @Override
                                           public void run() {
                           //image received, show view content and set image
                           try {
                               byte[] imageAsBytes;
                               imageAsBytes = Base64.decode(parsedBase64Image.getBytes(), Base64.DEFAULT);
                               image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
                               showViewContent();
                           }catch (IllegalArgumentException err){
                               Toast.makeText(context, "Received malformed image", Toast.LENGTH_LONG).show();
                               image.setImageDrawable(null);
                           }
                       }
                   });
                }
            });
            thread.start();
        } catch (JSONException e) {
            //handle JSON parsing error
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    image.post(new
                                       Runnable() {
                                           @Override
                                           public void run() {
                           Toast.makeText(context, "Received malformed json", Toast.LENGTH_LONG).show();
                                           }
                   });
                }
            });
            thread.start();
            e.printStackTrace();
        }
    }
}

