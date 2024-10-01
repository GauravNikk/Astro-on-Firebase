package com.cilguru.ui.other;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import java.util.Date;
import java.util.Properties;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cilguru.R;


public class Contact extends AppCompatActivity implements View.OnClickListener {

    ConstraintLayout joinl,ctus;
    EditText nameTextView,subj,yourcontact,descriptionTextView,emaill;
    Button clear,submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else { ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1); }
        }
        else { //permission is automatically granted on sdk<23 upon installation
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
              getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);;
        setContentView(R.layout.activity_contact);
        emaill=findViewById(R.id.email);
        nameTextView=findViewById(R.id.nameTextView);
        subj=findViewById(R.id.subj);
        yourcontact=findViewById(R.id.yourcontact);
        descriptionTextView=findViewById(R.id.descriptionTextView);
        submit=findViewById(R.id.submit);
        clear=findViewById(R.id.clear);

        submit.setOnClickListener(this);
        clear.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                emaill.setText("");
                nameTextView.setText("");
                subj.setText("");
                yourcontact.setText("");
                descriptionTextView.setText("");
                Toast.makeText(Contact.this, "Cleared", Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onClick(View v) {
        Date dt = new Date();
        int hours = dt.getHours();
        int min = dt.getMinutes();
        String time = null;
        if(hours>=1 || hours<=12){
            time="Good Morning";
        }else if(hours>=12 || hours<=16){
            time= "Good Afternoon";
        }else if(hours>=16 || hours<=21){
            time= "Good Evening";
        }else if(hours>=21 || hours<=24){
            time= "Good Night";
        }

        String to="gauravxxxxx@gmail.com";
        String subject=subj.getText().toString();
        String message=descriptionTextView.getText().toString();
        String name=nameTextView.getText().toString();
        String no=yourcontact.getText().toString().trim();
        String mssg= time+"Ma'am,\n"+"I am "+name+" and I want to concern about "+subject+"\n\nDetails : "+message+"\n\nName : "+name+"\n\nMobile No. : "+no;

        //firebase_req

        // sendEmail();
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
        email.putExtra(Intent.EXTRA_SUBJECT, "CILGURU "+subject);
        email.putExtra(Intent.EXTRA_TEXT, mssg);

//need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}