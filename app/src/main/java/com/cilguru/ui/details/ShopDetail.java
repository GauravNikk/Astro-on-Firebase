package com.cilguru.ui.details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cilguru.R;
import com.cilguru.model.ShopData;
import com.cilguru.model.YtData;
import com.cilguru.ui.MainActivity;
import com.cilguru.ui.loginreg.Login;
import com.cilguru.ui.other.Contact;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import xyz.hasnat.sweettoast.SweetToast;

public class ShopDetail extends AppCompatActivity {

Button buynoww;
    String mnn = "";
    private ValueEventListener mDBListener;
    TextView nametv,ftrtv,pricetv,desctv,jtv,buynow;
    ImageView imgtv;
    String name,price,ftr,img,desc,join,lang;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    //Firebase
    private List<ShopData> mTvdt;
    private DatabaseReference mDataref;
    public String tagg = null;
    private FirebaseAuth mAuth;
    public FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


              getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);;
        setContentView(R.layout.activity_shopdetail);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null){
            logOutUser(); // Return to Login activity
        }
        currentUser = mAuth.getCurrentUser();
        String user_id = mAuth.getCurrentUser().getUid();
        mDataref= FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
        mDataref.keepSynced(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else { ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1); }
        }
        else { //permission is automatically granted on sdk<23 upon installation
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        Intent i=this.getIntent();
        String pkey;
         name=i.getExtras().getString("PName");
        desc=i.getExtras().getString("PDesc");
         img=i.getExtras().getString("PImg");
        price=i.getExtras().getString("PPrice");
         ftr=i.getExtras().getString("PFtr");
       join=i.getExtras().getString("PJion");
       pkey=i.getExtras().getString("PKey");



        nametv=findViewById(R.id.nameTextView);
        ftrtv=findViewById(R.id.ftrtv);
        pricetv=findViewById(R.id.pricetv);
        jtv=findViewById(R.id.jtv);
        desctv=findViewById(R.id.descriptionTextView);
        imgtv=findViewById(R.id.imgfetch);
        buynow=findViewById(R.id.buynow);



        nametv.setText(name);
        desctv.setText(desc);

        Picasso.get()
                .load(img)
                .placeholder(R.drawable.itemimg)
                //.fit()
                // .centerCrop()
                // .load(imgurll)
                .into(imgtv);

        pricetv.setText(price);
        ftrtv.setText(ftr);
        jtv.setText(join);


        // This type of listener is not one time, and you need to cancel it to stop
        // receiving updates.
        mDataref = FirebaseDatabase.getInstance().getReference("Shop").child(pkey);
        mDataref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // retrieve data from db
                String name = dataSnapshot.child("pprice").getValue().toString();

//                if(name.equals("N/A")){
//                    buynow.setVisibility(View.GONE);
//                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }


        });

        Log.d("NAMEEE",name);

        tagg=name;


        loaddt();

    }
    public void loaddt() {
        String value = tagg;
        Log.d("loadddd",value);
        mAuth = FirebaseAuth.getInstance();
        String user_id = mAuth.getCurrentUser().getUid();

        Query qalk = FirebaseDatabase.getInstance().getReference("users").child(user_id).child("Buy").child(value);
        Log.d("loadddd",qalk.toString());

            qalk.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final Calendar calendar = Calendar.getInstance();
                    final Date date = calendar.getTime();
                    Integer day = Integer.valueOf(new SimpleDateFormat("dd").format(date));    // always 2 digits
                    Integer  month = Integer.valueOf(new SimpleDateFormat("MM").format(date));  // always 2 digits
                    Integer year = Integer.valueOf(new SimpleDateFormat("yyyy").format(date)); // 4 digit year
                    Log.d("Dateee",String.valueOf(day)+"\n"+String.valueOf(month)+"\n"+String.valueOf(month));




                        if (dataSnapshot.hasChild("payst")) {
                            String pst = dataSnapshot.child("payst").getValue().toString();
                            Log.d("Payt", pst);
                            if (pst!= null && pst.equals("Paid") ) {
                                buynow.setText("Purchased");
                                buynow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(ShopDetail.this, CourseVideo.class);
                                        Log.d("tggv",tagg+"by cvd");
                                        intent.putExtra("Pkey", tagg);
                                        startActivity(intent);
//                                        String[] fData = {name, desc, img, price, ftr, join};
//                                        // openDetailActivity(fData);
//                                        SweetToast.info(ShopDetail.this,"Please Pay First,then Wait for Approval\nIf Already Paid then ignore to pay but wait for approval");
                                    }
                                });
                            } else if (pst!= null && pst.equals("Unpaid")){
                                Log.d("Payt","unpaid");
                                buynow.setText("Not Paid");
                                buynow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
//                                        String[] fData = {name, desc, img, price, ftr, join};
//                                        openDetailActivity(fData);
                                        final Dialog dialog = new Dialog(ShopDetail.this);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setCanceledOnTouchOutside(false);
                                        dialog.setContentView(R.layout.pay);
                                        TextView cross=(TextView) dialog.findViewById(R.id.btnn);
                                        ImageView cimg=(ImageView) dialog.findViewById(R.id.cimg);

                                        cimg.setImageResource(R.drawable.payt);
                                        cross.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
//                                                Intent intent = new Intent(ShopDetail.this, SendActivity.class);
//                                                startActivity(intent);
                                                dialog.cancel();
                                            }
                                        });

                                        dialog.show();
                                        SweetToast.info(ShopDetail.this,"Please Pay First,then Wait for Approval\nIf Already Paid then ignore to pay but wait for approval");
                                    }
                                });
                            } else {
                                buynow.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String[] fData = {name, desc, img, price, ftr, join};
                                        openDetailActivity(fData);

                                    }
                                });
                            }

                        }else{
                            buynow.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String[] fData = {name, desc, img, price, ftr, join};
                                    openDetailActivity(fData);
                                    //                                    }
                                }
                            });
                        }



                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
    }
    private void logOutUser() {
        Intent loginIntent =  new Intent(this, Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
///        logOutUser();
        startActivity(loginIntent);
        finish();


    }
    private void openDetailActivity(String[] data) {
        Intent intent = new Intent(this, ShopContact.class);
        intent.putExtra("PName", data[0]);
        intent.putExtra("PDesc", data[1]);
        intent.putExtra("PImg", data[2]);
        intent.putExtra("PPrice", data[3]);
        intent.putExtra("PFtr", data[4]);
        intent.putExtra("PJion", data[5]);
      //  intent.putExtra("lang", data[6]);
        startActivity(intent);
        Log.d("fileintent","done");
    }
    private void openDetailActivity2(){
        Intent intent = new Intent(this, ShopContact.class);
        intent.putExtra("PName",name);
        intent.putExtra("PDesc",desc);
        intent.putExtra("PImg", img);
        intent.putExtra("PPrice", price);
        intent.putExtra("PFtr", ftr);
        intent.putExtra("PJion", join);
          intent.putExtra("lang",lang);
        startActivity(intent);
        Log.d("fileintent","done");
    }
}