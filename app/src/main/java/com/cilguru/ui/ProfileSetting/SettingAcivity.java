package com.cilguru.ui.ProfileSetting;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cilguru.R;
import com.cilguru.ui.details.MyPurchase;
import com.cilguru.ui.loginreg.Login;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import xyz.hasnat.sweettoast.SweetToast;

public class SettingAcivity extends AppCompatActivity {

    private CircleImageView profile_settings_image;
    private EditText display_name, display_email, user_phone, udob, user_display_name;

    private TextView logout, updatedMsg, umail;
    private ImageView editPhotoIcon, editStatusBtn;
    private RadioButton maleRB, femaleRB;

    private Button saveInfoBtn,purchase;

    private DatabaseReference getUserDatabaseReference;
    private FirebaseAuth mAuth;
    private StorageReference mProfileImgStorageRef;
    private StorageReference thumb_image_ref;

    private final static int GALLERY_PICK_CODE = 1;
    Bitmap thumb_Bitmap = null;

    private ProgressDialog progressDialog;
    private String selectedGender = "", profile_download_url, profile_thumb_download_url;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        String id = mAuth.getCurrentUser().getUid();
        getUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(id);
        getUserDatabaseReference.keepSynced(true); // for offline

        mProfileImgStorageRef = FirebaseStorage.getInstance().getReference().child("profile_image");
        thumb_image_ref = FirebaseStorage.getInstance().getReference().child("thumb_image");

        profile_settings_image = findViewById(R.id.profile_img);
        logout=findViewById(R.id.logout);
        user_display_name = findViewById(R.id.user_display_name );
        umail = findViewById(R.id.umail);
        user_phone = findViewById(R.id.uno);
        udob = findViewById(R.id.udob);
        purchase=findViewById(R.id.purchase);
        saveInfoBtn = findViewById(R.id.saveInfoBtn);
        updatedMsg = findViewById(R.id.updatedMsg);


        progressDialog = new ProgressDialog(this);

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

                user_display_name.setText(name);
                user_display_name.setSelection(user_display_name.getText().length());
                user_phone.setText(phone);
                user_phone.setSelection(user_phone.getText().length());
                umail.setText(email);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


        saveInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uName = user_display_name.getText().toString();
                String uPhone = user_phone.getText().toString();
                String dob=udob.getText().toString();

                saveInformation(uName, uPhone,dob);
            }
        });

        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent statusUpdateIntent = new Intent(SettingAcivity.this, MyPurchase.class);
                startActivity(statusUpdateIntent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent statusUpdateIntent = new Intent(SettingAcivity.this, Login.class);
                startActivity(statusUpdateIntent);
            }
        });
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    } // Ending onCrate

    private void saveInformation(String uName,  String uPhone, String dob) {
       if (TextUtils.isEmpty(uName)){
            SweetToast.error(this, "Oops! your name can't be empty");
        } else if (uName.length()<3 || uName.length()>40){
            SweetToast.warning(this, "Your name should be 3 to 40 numbers of characters");
        } else if (TextUtils.isEmpty(uPhone)){
            SweetToast.error(this, "Your mobile number is required.");
        } else if (uPhone.length()<10){
            SweetToast.warning(this, "Sorry! your mobile number is too short");
        } else {

            getUserDatabaseReference.child("user_name").setValue(uName);
            getUserDatabaseReference.child("user_mobile").setValue(uPhone);
            getUserDatabaseReference.child("user_image").setValue(dob)

                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updatedMsg.setVisibility(View.VISIBLE);

                            new Timer().schedule(new TimerTask(){
                                public void run() {
                                    SettingAcivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            updatedMsg.setVisibility(View.GONE);
                                        }
                                    });
                                }
                            }, 1500);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK_CODE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                progressDialog.setMessage("Please wait...");
                progressDialog.show();

                final Uri resultUri = result.getUri();

                File thumb_filePath_Uri = new File(resultUri.getPath());

                final String user_id = mAuth.getCurrentUser().getUid();

                try{
                    thumb_Bitmap = new Compressor(this)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .setQuality(45)
                            .compressToBitmap(thumb_filePath_Uri);
                } catch (IOException e){
                    e.printStackTrace();
                }


                // firebase storage for uploading the cropped image
                final StorageReference filePath = mProfileImgStorageRef.child(user_id + ".jpg");

                UploadTask uploadTask = filePath.putFile(resultUri);
                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task){
                        if (!task.isSuccessful()){
                            SweetToast.error(SettingAcivity.this, "Profile Photo Error: " + task.getException().getMessage());
                            //throw task.getException();
                        }
                        profile_download_url = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){

                            profile_download_url = task.getResult().toString();
                            Log.e("tag", "profile url: "+profile_download_url);

                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                            thumb_Bitmap.compress(Bitmap.CompressFormat.JPEG, 45, outputStream);
                            final byte[] thumb_byte = outputStream.toByteArray();

                            // firebase storage for uploading the cropped and compressed image
                            final StorageReference thumb_filePath = thumb_image_ref.child(user_id + "jpg");
                            UploadTask thumb_uploadTask = thumb_filePath.putBytes(thumb_byte);

                            Task<Uri> thumbUriTask = thumb_uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task){
                                    if (!task.isSuccessful()){
                                        SweetToast.error(SettingAcivity.this, "Thumb Image Error: " + task.getException().getMessage());
                                    }
                                    profile_thumb_download_url = thumb_filePath.getDownloadUrl().toString();
                                    return thumb_filePath.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    profile_thumb_download_url = task.getResult().toString();
                                    Log.e("tag", "thumb url: "+profile_thumb_download_url);
                                    if (task.isSuccessful()){
                                        Log.e("tag", "thumb profile updated");

                                        HashMap<String, Object> update_user_data = new HashMap<>();
                                        update_user_data.put("user_image", profile_download_url);
                                        update_user_data.put("user_thumb_image", profile_thumb_download_url);

                                        getUserDatabaseReference.updateChildren(new HashMap<String, Object>(update_user_data))
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.e("tag", "thumb profile updated");
                                                        progressDialog.dismiss();
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("tag", "for thumb profile: "+ e.getMessage());
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }

                                }
                            });


                        }

                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                SweetToast.info(SettingAcivity.this,"Image cropping failed.");
            }
        }

    }


}


