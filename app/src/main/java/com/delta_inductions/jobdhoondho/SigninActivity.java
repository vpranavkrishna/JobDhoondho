package com.delta_inductions.jobdhoondho;

import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;

import androidx.annotation.*;
import androidx.appcompat.app.*;

import com.google.android.gms.tasks.*;
import com.google.android.material.textfield.*;
import com.google.firebase.firestore.*;

import io.grpc.*;

public class SigninActivity extends AppCompatActivity {
    private TextInputEditText input_text;
    private Button btn_submit;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userref = db.collection("users");
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        btn_submit = findViewById(R.id.sub_btn);
        input_text = findViewById(R.id.username);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(input_text.getText().toString().isEmpty()))
                {
                           userref.document(input_text.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                               @Override
                               public void onSuccess(DocumentSnapshot documentSnapshot) {
                                   if(documentSnapshot.exists()) {
                                       Toast.makeText(getApplicationContext(), "User found", Toast.LENGTH_SHORT).show();
                                   String option = documentSnapshot.getString("option");
                                   String name = documentSnapshot.getString("username");
                                   Intent intent = new Intent(SigninActivity.this,UserActivity.class);
                                   intent.putExtra("option",option);
                                   intent.putExtra("name",name);
                                   startActivity(intent);
                                   finish();
                                   }
                                   else
                                   {
                                       Toast.makeText(getApplicationContext(), "User not found,New user", Toast.LENGTH_SHORT).show();
                                       startActivity(new Intent(SigninActivity.this,WorkerOrEmployer.class));
                                       finish();

                                   }
                               }
                           });
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "username cant be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
