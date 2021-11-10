package com.delta_inductions.jobdhoondho;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class UserActivity extends AppCompatActivity {
    private static final String TAG = "UserActivity";
    TextView toolbartext;
    Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        toolbar = findViewById(R.id.toolbar);
        toolbartext = findViewById(R.id.toolbartext);
        toolbar.inflateMenu(R.menu.menu_signout);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,Home_Fragment.newInstance(getIntent().getStringExtra("option"),getIntent().getStringExtra("name"))).commit();
       bottomNavigationView = findViewById(R.id.bottom_navigation);
       bottomNavigationView.setOnNavigationItemSelectedListener(navlistner);
       toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
           @Override
           public boolean onMenuItemClick(MenuItem item) {
               if(item.getItemId()==R.id.signout)
               {
                   Toast.makeText(UserActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                   FirebaseAuth.getInstance().signOut();
                   finish();
               }
               return false;
           }
       });
    }
private BottomNavigationView.OnNavigationItemSelectedListener navlistner = new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedfragment = null;
        switch(item.getItemId())
        {
            case R.id.nav_home: {
                selectedfragment = Home_Fragment.newInstance(getIntent().getStringExtra("option"),getIntent().getStringExtra("name"));
                toolbartext.setText("Home");
            }
                break;

            case R.id.nav_chat: {
                selectedfragment = new Chat_Fragments();
                toolbartext.setText("Chat");
            }
                break;
            case R.id.nav_profile: {
                selectedfragment =  Profile_Fragment.newInstance(getIntent().getStringExtra("name"));
                toolbartext.setText("Profile");
            }
                break;

        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container,selectedfragment).commit();
        return true;
    }
};
    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        finishAffinity();
    }
}