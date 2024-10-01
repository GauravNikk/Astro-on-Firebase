package com.cilguru.firebase.fcm;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cilguru.R;


public class SecondActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.send);

		TextView txt = findViewById(R.id.txt);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			for (String key : bundle.keySet()) {
				Object value = bundle.get(key);
				txt.append(key + ": " + value + "\n\n");
			}
		}
	}
}