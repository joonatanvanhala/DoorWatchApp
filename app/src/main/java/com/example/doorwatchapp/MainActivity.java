package com.example.doorwatchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;



public class MainActivity extends AppCompatActivity {
    private AwsIotConnection iotConnection;
    private static TextView text_view_id;
    private static Button no;
    public static Button yes;
    public static ImageView image;

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
        yes.setText("Kyllä");
        no.setText("Ei");
        text_view_id.setText("Päästetäänkö sisään?");
        //initially hide content
        hideViewContent();
        //Create new connection to aws iot service
        iotConnection = new AwsIotConnection();
        iotConnection.connect(getApplicationContext());
        iotConnection.subscribe(this.getApplicationContext());
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    public void openDoor(View v) {
        Toast.makeText(this, "Opening door for customer!", Toast.LENGTH_LONG).show();
        String message = "in";
        iotConnection.publish(message);
        //Remove image from view after user has made selection
        image = findViewById(R.id.image);
        image.setImageDrawable(null);
        hideViewContent();
    }
    public void closeDoor(View v) {
        Toast.makeText(this, "Not letting customer in!", Toast.LENGTH_LONG).show();
        String message = "out";
        iotConnection.publish(message);
        //Remove image from view after user has made selection
        image = findViewById(R.id.image);
        image.setImageDrawable(null);
        hideViewContent();
    }
    public static void hideViewContent() {
        System.out.println("hideview");
        text_view_id.setVisibility(View.INVISIBLE);
        no.setVisibility(View.INVISIBLE);
        yes.setVisibility(View.INVISIBLE);
        image.setVisibility(View.INVISIBLE);
    }
    public static void showViewContent() {
        System.out.println("showview");
        text_view_id.setVisibility(View.VISIBLE);
        no.setVisibility(View.VISIBLE);
        yes.setVisibility(View.VISIBLE);
        image.setVisibility(View.VISIBLE);
    }
}

