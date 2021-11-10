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

import com.delta_inductions.jobdhoondho.Model.ProfileWorker;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewProfileWorker extends AppCompatActivity implements View.OnClickListener {
private EditText username;
private EditText occupation;
private EditText experience;
private EditText mobilenumber;
private String usernameinput;
private String occupationinput;
private String mobilenumberinput;
private String experienceinput;
private Button createprofile;
private ProfileWorker newprofileworker;
private FirebaseFirestore db = FirebaseFirestore.getInstance();
private CollectionReference userref = db.collection("users");
private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile_worker);
        username = findViewById(R.id.username);
        occupation = findViewById(R.id.occupation);
        experience = findViewById(R.id.experience);
        mobilenumber = findViewById(R.id.mobilenumber);
        createprofile = findViewById(R.id.newprofile);
        username.addTextChangedListener(textWatcher);
        occupation.addTextChangedListener(textWatcher);
        experience.addTextChangedListener(textWatcher);
        mobilenumber.addTextChangedListener(textWatcher);
        createprofile.setOnClickListener(this);
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            usernameinput = username.getText().toString().trim();
            occupationinput = occupation.getText().toString().trim();
            mobilenumberinput = mobilenumber.getText().toString().trim();
            experienceinput = experience.getText().toString().trim();

            createprofile.setEnabled((!usernameinput.isEmpty())&&(!occupationinput.isEmpty())&&(!mobilenumberinput.isEmpty())&&(!experienceinput.isEmpty()));
            if(createprofile.isEnabled())
            {
                createprofile.setTextColor(Color.parseColor("#ffffff"));
            }
            else
            {
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
            newprofileworker = new ProfileWorker(usernameinput, occupationinput, experienceinput, mobilenumberinput,"seeker",null);
            userref.document(usernameinput).set(newprofileworker).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(NewProfileWorker.this, "Profile created", Toast.LENGTH_SHORT).show();
                    intent = new Intent(NewProfileWorker.this, UserActivity.class);
                    intent.putExtra("option", "seeker");
                    intent.putExtra("name",usernameinput);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(NewProfileWorker.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            Toast.makeText(this, "Enter a 10 digit mobile number", Toast.LENGTH_SHORT).show();
        }
    }
}