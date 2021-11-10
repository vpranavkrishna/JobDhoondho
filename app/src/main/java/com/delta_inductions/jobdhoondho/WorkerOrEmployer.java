package com.delta_inductions.jobdhoondho;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WorkerOrEmployer extends AppCompatActivity implements View.OnClickListener{
private CardView employer;
private CardView worker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_or_employer);
        employer = findViewById(R.id.employer);
        worker = findViewById(R.id.worker);
        employer.setOnClickListener(this);
        worker.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
          switch (v.getId()) {
              case R.id.employer: {
                  Intent intent = new Intent(this, NewProfileEmployer.class);
                  intent.putExtra("option", "recruiter");
                  startActivity(intent);
                  finish();
              }
              break;
              case R.id.worker :
              {
                  Intent intent = new Intent(this, NewProfileWorker.class);
                  intent.putExtra("option", "seeker");
                  startActivity(intent);
                  finish();
              }
          }

    }
}