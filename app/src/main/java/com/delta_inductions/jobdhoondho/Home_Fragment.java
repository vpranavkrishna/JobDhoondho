package com.delta_inductions.jobdhoondho;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.delta_inductions.jobdhoondho.Adapter.PostAdapter;
import com.delta_inductions.jobdhoondho.Model.Post;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import java.util.ArrayList;

public class Home_Fragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "Home_Fragment";
    private static final String ARG_OPTION = "option";
    private static final String ARG_NAME = "name";
    private View V;
    private String option;
    private String name;
    private RecyclerView recyclerView;
    private ListenerRegistration postlistner;
    private FloatingActionButton fab;
    private FirestoreRecyclerOptions<Post> postsrec;
    private ProgressBar progressBar;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userref = db.collection("users");
    private CollectionReference allposts = db.collection("allposts");
    private PostAdapter adapter;
    private String username;
    private String experience;
    private String occupation;
    private String mobilenumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        V = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = V.findViewById(R.id.homerecycle);
        fab = V.findViewById(R.id.fab);
        progressBar = V.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        if (getArguments() != null) {
            option = getArguments().getString(ARG_OPTION);
            name = getArguments().getString(ARG_NAME);
            setupRecyclerView();
        }

        fab.setOnClickListener(this);
        return V;
    }

    public static Home_Fragment newInstance(String option ,String name) {
        Home_Fragment fragment = new Home_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_OPTION, option);
        args.putString(ARG_NAME,name);
        fragment.setArguments(args);
        return fragment;
    }


    private void setupRecyclerView() {
        progressBar.setVisibility(View.GONE);
        if (option.equals("recruiter")) {
            Log.d(TAG, "onSuccess: recruiter");
            Query query = userref.document(name).collection("posts").orderBy("dateofpost", Query.Direction.DESCENDING);
            postsrec = new FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post.class).build();
        } else {
            Query query = allposts.orderBy("dateofpost", Query.Direction.DESCENDING);
            postsrec = new FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post.class).build();
            fab.setVisibility(View.GONE);
        }
        adapter = new PostAdapter(postsrec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnitemclicklistner(new PostAdapter.OnItemClicklistener() {
            @Override
            public void OnItemClick(DocumentSnapshot documentSnapshot, int position) {
                String empnumber =documentSnapshot.getString("mobilenumber");
                String positiontext = documentSnapshot.getString("position");
//                String dateofpost = String.valueOf(documentSnapshot.getTimestamp("dateofpost"));
                userref.document(name).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                          @Override
                                                          public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                              username = documentSnapshot.getString("username");
                                                              experience = documentSnapshot.getString("experience");
                                                              occupation =documentSnapshot.getString("occupation");
                                                              mobilenumber = documentSnapshot.getString("mobilenumber");
                                                              String messagedetail = positiontext + " job .I am" +username+",I have an experience of " + experience + " in the field of " + occupation
                                                                      + " my mobile number is " + mobilenumber + " feel free to contact me.";
                                                              openWhatsApp(empnumber,messagedetail);
                                                          }
                                                      });
            }
        });
    }

    @Override
    public void onClick(View v) {
       Intent intent =  new Intent(getActivity(), CreateJob.class);
       intent.putExtra("name",name);
       startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            option = getArguments().getString(ARG_OPTION);
        }
        setupRecyclerView();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void openWhatsApp(String number, String applypos) {
        try {
            String text = "Hi I am applying for ";
           text =  text.concat(applypos);// Replace with your message.

            String toNumber = "91";
           toNumber = toNumber.concat(number);// Replace with mobile phone number without +Sign or leading zeros, but with country code
            //Suppose your country is India and your phone number is “xxxxxxxxxx”, then you need to send “91xxxxxxxxxx”.
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("http://api.whatsapp.com/send?phone=" + toNumber + "&text=" + text));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
