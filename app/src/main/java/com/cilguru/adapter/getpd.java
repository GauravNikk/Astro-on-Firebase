package com.cilguru.adapter;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.cilguru.ui.details.CourseVideo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class getpd {
    private Toolbar mToolbar;
    private Button sendFriendRequest_Button, declineFriendRequest_Button;
    private TextView profileName, profileStatus, u_work, go_my_profile;
    private ImageView profileImage, verified_icon;
    String name;
    private DatabaseReference userDatabaseReference;

    private DatabaseReference friendRequestReference;
    private FirebaseAuth mAuth;
    public static String CURRENT_STATE;

    public String userID = mAuth.getCurrentUser().getUid(); // Visited profile's id


    private DatabaseReference friendsDatabaseReference;
    private DatabaseReference notificationDatabaseReference;



public void  getcc(){
 //  CourseVideo v = new CourseVideo();
    userDatabaseReference=CourseVideo.mDatabaseRef2;
    Log.d("mdtrf",userDatabaseReference.toString());

    // userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
    userDatabaseReference.keepSynced(true); // for offline

    userDatabaseReference.child(userID).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            name = dataSnapshot.child("payst").getValue().toString();

            if (name.equals(null)||name.equals("Unpaid")){
                CURRENT_STATE="1";
            }else{
                CURRENT_STATE="0";
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });
}
}
