package com.delta_inductions.jobdhoondho;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

public class Profile_Fragment extends Fragment {
    private static final String TAG = "Profile_Fragment";
    private static final String ARG_NAME = "name";
    private View V;
    private TextView username;
    private TextView companyname;
    private TextView companynametitle;
    private TextView designation;
    private TextView designationtitle;
    private TextView mobilenumber;
    private TextView mobilenumbertitle;
    private ProgressBar progressBar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference useref = db.collection("users");
    private String name;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
        }
        LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.Loadingalert();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadingDialog.dismissdialog();
            }
        }, 1000);

        V = inflater.inflate(R.layout.fragment_profile, container, false);
        username = V.findViewById(R.id.username);
        companynametitle = V.findViewById(R.id.companytitle);
        companyname = V.findViewById(R.id.companyname);
        designationtitle = V.findViewById(R.id.designationtitle);
        designation = V.findViewById(R.id.designation);
        mobilenumber = V.findViewById(R.id.mobilenumber);
        progressBar = V.findViewById(R.id.progress_bar);
        useref.document(name).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                progressBar.setVisibility(View.GONE);
                if (documentSnapshot.exists()) {
                    if (documentSnapshot.getString("option").equals("seeker")) {
                        companynametitle.setText("Occupation");
                        designationtitle.setText("Experience");
                        companyname.setText(documentSnapshot.getString("occupation"));
                        designation.setText(documentSnapshot.getString("experience"));
                        mobilenumber.setText(documentSnapshot.getString("mobilenumber"));
                        username.setText(documentSnapshot.getString("username"));
                    } else {
                        companyname.setText(documentSnapshot.getString("companyname"));
                        designation.setText(documentSnapshot.getString("designation"));
                        mobilenumber.setText(documentSnapshot.getString("mobilenumber"));
                        username.setText(documentSnapshot.getString("username"));
                    }
                } else {
                    Toast.makeText(getContext(), "Document doesnot exists ", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
        return V;
    }
    public static Profile_Fragment newInstance(String name)
    {
        Profile_Fragment fragment = new Profile_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME,name);
        fragment.setArguments(args);
        return fragment;
    }
}
