package com.delta_inductions.jobdhoondho;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.delta_inductions.jobdhoondho.Model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class CreateJob extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "CreateJob";
    private static final String KEY_POSITION ="position";
    private static final String KEY_SALARY="salary";
    private static final String [] positions = new String[]{"Fitter","Fire Watcher","Helper","Rigger","Welder"};
    private FirebaseFirestore db =  FirebaseFirestore.getInstance();
    private EditText position;
    private EditText salary;
    private String mobilenumber;
    private AutoCompleteTextView positionslist;
    private ArrayAdapter<String> adapter;
    private EditText location;
    private EditText Work_period;
    private EditText Description;
    private boolean clickedonce = false;
    private Button post;
    private ProgressBar progressBar;
    private String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job);
        position = findViewById(R.id.position);
        salary = findViewById(R.id.salary);
        location = findViewById(R.id.location);
        Work_period =  findViewById(R.id.work_period);
        Description = findViewById(R.id.job_description);
        post = findViewById(R.id.btn_post);
        progressBar = findViewById(R.id.progressBar);
        salary.addTextChangedListener(textWatcher);
        location.addTextChangedListener(textWatcher);
        Work_period.addTextChangedListener(textWatcher);
        Description.addTextChangedListener(textWatcher);
        post.setOnClickListener(this);
        name = getIntent().getStringExtra("name");
    }
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
             String positioninput = position.getText().toString().trim();
             String salaryinput = salary.getText().toString().trim();
             String locationinput = location.getText().toString().trim();
             String work_period = Work_period.getText().toString().trim();
             String description = Description.getText().toString().trim();
             post.setEnabled((!positioninput.isEmpty())&&(!salaryinput.isEmpty())&&(!locationinput.isEmpty())&&(!work_period.isEmpty())
             &&(!description.isEmpty()));
             if(post.isEnabled())
             {
                 post.setTextColor(Color.parseColor("#ffffff"));
             }
             else
             {
                 post.setTextColor(1107296256);
             }
        }
        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    @Override
    public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            String positiontext = position.getText().toString();
            String salarytext = salary.getText().toString();
            String locationtext = location.getText().toString();
            String work_periodtext = Work_period.getText().toString();
            String descriptiontext = Description.getText().toString();
            Log.d(TAG, "onClick: "+name);
            CollectionReference Postref = FirebaseFirestore.getInstance().collection("users").document(getIntent().getStringExtra("name")).collection("posts");
            CollectionReference allpost = FirebaseFirestore.getInstance().collection("allposts");
            DocumentReference profileref = FirebaseFirestore.getInstance().collection("users").document(getIntent().getStringExtra("name"));
            profileref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists())
                    {
                        mobilenumber = documentSnapshot.getString("mobilenumber");
                        Log.d(TAG, "onSuccess: "+mobilenumber);
                        Postref.add(new Post(positiontext,salarytext,locationtext,work_periodtext,descriptiontext, Timestamp.now().toDate(),documentSnapshot.getString("mobilenumber"),false,name));
                        allpost.add(new Post(positiontext,salarytext,locationtext,work_periodtext,descriptiontext, Timestamp.now().toDate(),documentSnapshot.getString("mobilenumber"),true,name));

                    }
                     else
                        Toast.makeText(CreateJob.this, "document not found", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
            Toast.makeText(this, "Job posted", Toast.LENGTH_SHORT).show();
            goback();
    }
    private void goback()
    {
        super.onBackPressed();
    }
}