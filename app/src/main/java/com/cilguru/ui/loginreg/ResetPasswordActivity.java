package com.cilguru.ui.loginreg;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cilguru.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


import java.util.Timer;
import java.util.TimerTask;

import xyz.hasnat.sweettoast.SweetToast;


public class ResetPasswordActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText forgotEmail;
    private Button resetPassButton;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
              getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);;
        setContentView(R.layout.activity_reset_password);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            } else { ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1); }
        }
        else { //permission is automatically granted on sdk<23 upon installation
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }


        auth = FirebaseAuth.getInstance();

        forgotEmail = findViewById(R.id.userEmail);
        resetPassButton = findViewById(R.id.loginButton);
        resetPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = forgotEmail.getText().toString();
                if(TextUtils.isEmpty(email)){
                    SweetToast.error(ResetPasswordActivity.this, "Email is required");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    SweetToast.error(ResetPasswordActivity.this,"Email format is not valid.");
                } else {
                    // send email to reset password
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                emailSentSuccessPopUp();

                                // LAUNCH activity after certain time period
                                new Timer().schedule(new TimerTask(){
                                    public void run() {
                                        ResetPasswordActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                auth.signOut();

                                                Intent mainIntent =  new Intent(ResetPasswordActivity.this, Login.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(mainIntent);
                                                finish();

                                                SweetToast.info(ResetPasswordActivity.this, "Please check your email.");

                                            }
                                        });
                                    }
                                }, 8000);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            SweetToast.error(ResetPasswordActivity.this, "Oops!! "+e.getMessage());
                        }
                    });
                }
            }
        });

    }

    private void emailSentSuccessPopUp() {
        // Custom Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
        View view = LayoutInflater.from(ResetPasswordActivity.this).inflate(R.layout.register_success_popup, null);
        TextView successMessage = view.findViewById(R.id.successMessage);
        successMessage.setText("Password reset link has been sent successfully.\nPlease check your email. Thank You.");
        builder.setCancelable(true);

        builder.setView(view);
        builder.show();
    }

}
