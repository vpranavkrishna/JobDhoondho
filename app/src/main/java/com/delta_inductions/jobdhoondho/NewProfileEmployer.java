package com.delta_inductions.jobdhoondho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.delta_inductions.jobdhoondho.Model.ProfileEmployer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewProfileEmployer extends AppCompatActivity implements View.OnClickListener {
    private EditText username;
    private EditText companyname;
    private EditText mobilenumber;
    private EditText designation;
    private Button createprofile;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean clickedonce = false;
    private CollectionReference userref = db.collection("users");
    private String usernameinput;
    private String companynameinput;
    private String mobilenumberinput;
    private String designationinput;
    private ProfileEmployer newProfileEmployer;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);
        username = findViewById(R.id.username);
        companyname = findViewById(R.id.companyname);
        mobilenumber = findViewById(R.id.mobilenumber);
        designation = findViewById(R.id.designation);
        createprofile = findViewById(R.id.newprofile);
        username.addTextChangedListener(textWatcher);
        companyname.addTextChangedListener(textWatcher);
        mobilenumber.addTextChangedListener(textWatcher);
        designation.addTextChangedListener(textWatcher);
        createprofile.setOnClickListener(this);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            usernameinput = username.getText().toString().trim();
            companynameinput = companyname.getText().toString().trim();
            mobilenumberinput = mobilenumber.getText().toString().trim();
            designationinput = designation.getText().toString().trim();

            createprofile.setEnabled((!usernameinput.isEmpty()) && (!companynameinput.isEmpty()) && (!mobilenumberinput.isEmpty()) && (!designationinput.isEmpty()));
            if (createprofile.isEnabled()) {
                createprofile.setTextColor(Color.parseColor("#ffffff"));
            } else {
                createprofile.setTextColor(1107296256);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void onClick(View v) {
        if (mobilenumberinput.length() == 10) {
            newProfileEmployer = new ProfileEmployer(usernameinput, companynameinput, mobilenumberinput, designationinput,null, "recruiter");
            userref.document(usernameinput).set(newProfileEmployer).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(NewProfileEmployer.this, "Profile created", Toast.LENGTH_SHORT).show();
                    intent = new Intent(NewProfileEmployer.this, UserActivity.class);
                    intent.putExtra("option","recruiter");
                    intent.putExtra("name",usernameinput);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NewProfileEmployer.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Enter a 10 digit mobile number", Toast.LENGTH_SHORT).show();
        }

    }
}