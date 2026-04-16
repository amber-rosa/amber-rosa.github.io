package com.cs360.inventoryappdelarosa;

import android.Manifest;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.telephony.SmsManager;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//Code for sending SMS message.
public class SmsPermissionActivity extends AppCompatActivity {
    TextView textView;
    EditText editTextPhone;
    EditText editTextMessage;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sms_permission);

        textView = findViewById(R.id.textViewMSG);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextMessage = findViewById(R.id.editTextMessage);

        //Button button = findViewById(R.id.buttonSMS);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(
                        editTextPhone.getText().toString(),
                        null,
                        editTextMessage.getText().toString(),
                        null,
                        null);

                textView.setText("SMS sent");
            }
        });
        // check if permissions have been granted and ask for them
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("smsPermissionActivity", "Permission is not granted, requesting");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 123);
            button.setEnabled(false);
        } else {
            Log.d("smsPermissionActivity", "Permission is granted");
        }
    }

    // check for response  if granted enable sms msgs else disable msgs
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("smsPermissionActivity", "Permission has been granted");
                textView.setText("You can send SMS!");
                button.setEnabled(true);
            } else {
                Log.d("smsPermissionActivity", "Permission has been denied or request cancelled");
                textView.setText("You can not send SMS!");
                button.setEnabled(false);
            }
        }

    }
}