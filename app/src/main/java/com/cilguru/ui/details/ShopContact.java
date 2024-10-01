package com.cilguru.ui.details;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cilguru.R;
import com.cilguru.model.Req;
import com.cilguru.model.YtData;
import com.cilguru.ui.MainActivity;
import com.cilguru.ui.ProfileSetting.SettingAcivity;
import com.cilguru.ui.loginreg.Login;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ShopContact extends AppCompatActivity implements View.OnClickListener {
    private static final String AUTH_KEY = "key=AAAAvfRU7Qk:APA91bHW6nJMmbVWT48RFwV81WnTrot6LAvUzogrLNp3aUgMHOx-x9pW32EqHQ65NBJCP1ySEXtlSRNCJAyjTD8_3nax9d5ccY96bxvVaRPDy2c2fAFmIyPiXluQkBPFqOpJ0g8zxbT5";

    private DatabaseReference mDatabaseRef,mDatabaseRef2;
    private ValueEventListener mDBListener;
    ConstraintLayout joinl,ctuss;
    EditText nameTextView,subj,yourcontact,descriptionTextView,emaill;
    Button clear,submit;
    private DatabaseReference getUserDatabaseReference;
    private FirebaseAuth mAuth;
    ImageView close;
    private String token;

    private StorageReference mProfileImgStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);;
        setContentView(R.layout.shop_contact);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else { ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1); }
        }
        else { //permission is automatically granted on sdk<23 upon installation
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();

        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
//        mDatabaseRef.keepSynced(true);
        ctuss=findViewById(R.id.ctuss);
        joinl=findViewById(R.id.joinl);
        emaill=findViewById(R.id.email);
        nameTextView=findViewById(R.id.nameTextView);
        subj=findViewById(R.id.subj);
        yourcontact=findViewById(R.id.yourcontact);
        descriptionTextView=findViewById(R.id.descriptionTextView);
        submit=findViewById(R.id.submit);
        clear=findViewById(R.id.clear);
        close=findViewById(R.id.close);
        ctuss.setVisibility(View.GONE);
        joinl.setVisibility(View.VISIBLE);

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
                Toast.makeText(ShopContact.this, "Cleared", Toast.LENGTH_SHORT).show();

            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {

                Intent statusUpdateIntent = new Intent(ShopContact.this, MainActivity.class);
                startActivity(statusUpdateIntent);

            }
        });
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    token = task.getException().getMessage();
                    Log.w("FCM TOKEN Failed", task.getException());
                } else {
                    token = task.getResult().getToken();
                    Log.i("FCM TOKEN", token);
                }
            }
        });
    }

    public void subscribe(View view) {
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        //   mTextView.setText(R.string.subscribed);
    }

    public void sendToken(View view) {
        sendWithOtherThread("token");
    }

    public void sendTokens(View view) {
        sendWithOtherThread("tokens");
    }

    public void sendTopic(View view) {
        sendWithOtherThread("topic");
    }

    private void sendWithOtherThread(final String type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification();
            }
        }).start();
    }
    @Override
    public void onClick(View v) {

        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();
        getUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
        getUserDatabaseReference.keepSynced(true); // for offline

        getUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // retrieve data from db
                String name = dataSnapshot.child("user_name").getValue().toString();
                String nickname = dataSnapshot.child("user_nickname").getValue().toString();
                String profession = dataSnapshot.child("user_profession").getValue().toString();
                String status = dataSnapshot.child("user_status").getValue().toString();
                String email = dataSnapshot.child("user_email").getValue().toString();
                String phone = dataSnapshot.child("user_mobile").getValue().toString();
                String gender = dataSnapshot.child("user_gender").getValue().toString();
                final String image = dataSnapshot.child("user_image").getValue().toString();
                String thumbImage = dataSnapshot.child("user_thumb_image").getValue().toString();

                //   display_status.setText(status);

                nameTextView.setText(name);
                nameTextView.setSelection(nameTextView.getText().length());

                yourcontact.setText(phone);
                yourcontact.setSelection(yourcontact.getText().length());

                emaill.setText(email);



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        //When we just slightly change the date format pattern, using hh for hour in am/pm (1-12) instead of HH for hour in day (0-23):
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss a");


        Date dt = new Date();
        int hours = dt.getHours();
        int min = dt.getMinutes();
        int am=dt.getTimezoneOffset();
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
        Intent i=this.getIntent();
        final String pname=i.getExtras().getString("PName");
        final String pdesc=i.getExtras().getString("PDesc");
        final String pimg=i.getExtras().getString("PImg");
        final String pprice=i.getExtras().getString("PPrice");
        final String ftr=i.getExtras().getString("PFtr");
        final String join=i.getExtras().getString("PJion");
        //    lang = i.getExtras().getString("lang");


        String to="cilguru1309@gmail.com";
        String subject=pname;
        String message=descriptionTextView.getText().toString();
        String name=nameTextView.getText().toString();
        String no=yourcontact.getText().toString().trim();
        String pymtst="Unpaid";
        String mssg= time+" Sir/Mam,\n"+"I am "+name+" and I want to purchase your course "+pname+
                "\n\nName : "+name+"\n\nMobile No. : "+no+"\n\n"+"Course : "+pname+"\n\n"+"Address : "+message;

        //firebase_req

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        String eml = user.getEmail();



        Req usersub = new Req(subject,pprice,pimg,name,no,eml,message,pymtst,uid);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Req");
        mDatabaseRef.push().setValue(usersub);
        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference("users").child(uid).child("Buy");
        mDatabaseRef2.child(pname).setValue(usersub);

        // startActivity(Intent.createChooser(email, "Choose an Email client :"));


        new Thread(new Runnable() {
            @Override
            public void run() {
                pushNotification();
            }
        }).start();
        // sendEmail();
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, mssg);

//need this to prompts email client only
        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));


       // if (intent.resolveActivity(getPackageManager()) != null) {
        joinl.setVisibility(View.GONE);
        ctuss.setVisibility(View.VISIBLE);

      //  startActivity(Intent.createChooser(email, "Choose an Email client :"));
      //  }
        joinl.setVisibility(View.GONE);
        ctuss.setVisibility(View.VISIBLE);
    }


    private void pushNotification() {
        String name=nameTextView.getText().toString();
        String no=yourcontact.getText().toString().trim();
        Intent i=this.getIntent();
        final String pname=i.getExtras().getString("PName");
        final String pdesc=i.getExtras().getString("PDesc");
        final String pimg=i.getExtras().getString("PImg");
        final String pprice=i.getExtras().getString("PPrice");
        final String ftr=i.getExtras().getString("PFtr");
        final String join=i.getExtras().getString("PJion");

        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jData = new JSONObject();
        try {
            jNotification.put("title", "Purchase");
            jNotification.put("body", name+" is purchased "+pname);
            jNotification.put("sound", "default");
            jNotification.put("badge", "1");
            jNotification.put("click_action", "OPEN_ACTIVITY");
            jNotification.put("icon", "ic_notification");

            jData.put("picture", pimg);

//            switch(type) {
//                case "tokens":
//                    JSONArray ja = new JSONArray();
//                    ja.put("c5pBXXsuCN0:APA91bH8nLMt084KpzMrmSWRS2SnKZudyNjtFVxLRG7VFEFk_RgOm-Q5EQr_oOcLbVcCjFH6vIXIyWhST1jdhR8WMatujccY5uy1TE0hkppW_TSnSBiUsH_tRReutEgsmIMmq8fexTmL");
//                    ja.put(token);
//                    jPayload.put("registration_ids", ja);
//                    break;
//                case "topic":
//                    jPayload.put("to", "/topics/news");
//                    break;
//                case "condition":
//                    jPayload.put("condition", "'sport' in topics || 'news' in topics");
//                    break;
//                default:
            jPayload.put("to", "/topics/news");
          //  }

            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jData);

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", AUTH_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // Send FCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jPayload.toString().getBytes());

            // Read FCM response.
            InputStream inputStream = conn.getInputStream();
            final String resp = convertStreamToString(inputStream);

            Handler h = new Handler(Looper.getMainLooper());
            h.post(new Runnable() {
                @Override
                public void run() {
                  //  mTextView.setText(resp);
                }
            });
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
    private String convertStreamToString(InputStream is) {
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next().replace(",", ",\n") : "";
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}